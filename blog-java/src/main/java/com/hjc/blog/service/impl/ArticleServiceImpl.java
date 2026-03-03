package com.hjc.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjc.blog.common.exception.BusinessException;
import com.hjc.blog.common.result.ResultCodeEnum;
import com.hjc.blog.dto.ArticleQueryDto;
import com.hjc.blog.dto.ArticleDto;
import com.hjc.blog.entity.Article;
import com.hjc.blog.entity.ArticleTag;
import com.hjc.blog.entity.Category;
import com.hjc.blog.entity.Tag;
import com.hjc.blog.entity.User;
import com.hjc.blog.mapper.ArticleMapper;
import com.hjc.blog.mapper.ArticleTagMapper;
import com.hjc.blog.mapper.CategoryMapper;
import com.hjc.blog.mapper.TagMapper;
import com.hjc.blog.mapper.UserMapper;
import com.hjc.blog.service.ArticleService;
import com.hjc.blog.vo.ArticleListVo;
import com.hjc.blog.vo.ArticleVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章服务实现类
 */
@Slf4j
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private ArticleTagMapper articleTagMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long publishArticle(ArticleDto request, Long authorId) {
        // 验证分类是否存在
        Category category = categoryMapper.selectById(request.getCategoryId());
        if (category == null || category.getStatus() == 0) {
            throw new BusinessException("分类不存在或已禁用");
        }

        // 创建文章
        Article article = new Article();
        BeanUtils.copyProperties(request, article);
        article.setAuthorId(authorId);
        article.setViewCount(0);
        article.setLikeCount(0);
        article.setCommentCount(0);
        article.setCollectCount(0);
        article.setIsTop(0);
        article.setIsFeatured(0);

        // 如果是发布状态，设置发布时间
        if (request.getStatus() != null && request.getStatus() == 1) {
            article.setPublishTime(LocalDateTime.now());
        }

        save(article);

        // 处理标签关联
        if (!CollectionUtils.isEmpty(request.getTagIds())) {
            saveArticleTags(article.getId(), request.getTagIds());
        }

        // 更新分类文章数
        updateCategoryArticleCount(request.getCategoryId(), 1);

        log.info("发布文章成功，文章ID: {}, 作者ID: {}", article.getId(), authorId);
        return article.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(Long id, ArticleDto request, Long userId, boolean isAdmin) {
        Article article = getAndCheckArticle(id);

        // 权限检查：只有作者或管理员可以修改
        checkPermission(article, userId, isAdmin);

        Long oldCategoryId = article.getCategoryId();

        // 验证分类是否存在
        Category category = categoryMapper.selectById(request.getCategoryId());
        if (category == null || category.getStatus() == 0) {
            throw new BusinessException("分类不存在或已禁用");
        }

        // 更新文章信息
        BeanUtils.copyProperties(request, article);
        article.setId(id);

        // 如果从草稿变为发布，设置发布时间
        if (request.getStatus() != null && request.getStatus() == 1 && article.getPublishTime() == null) {
            article.setPublishTime(LocalDateTime.now());
        }

        updateById(article);

        // 更新标签关联：先删除旧的，再添加新的
        articleTagMapper.deleteByArticleId(id);
        if (!CollectionUtils.isEmpty(request.getTagIds())) {
            saveArticleTags(id, request.getTagIds());
        }

        // 如果分类变更，更新分类文章数
        if (!oldCategoryId.equals(request.getCategoryId())) {
            updateCategoryArticleCount(oldCategoryId, -1);
            updateCategoryArticleCount(request.getCategoryId(), 1);
        }

        log.info("更新文章成功，文章ID: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(Long id, Long userId, boolean isAdmin) {
        Article article = getAndCheckArticle(id);

        // 权限检查：只有作者或管理员可以删除
        checkPermission(article, userId, isAdmin);

        // 删除文章标签关联
        articleTagMapper.deleteByArticleId(id);

        // 删除文章
        removeById(id);

        // 更新分类文章数
        updateCategoryArticleCount(article.getCategoryId(), -1);

        log.info("删除文章成功，文章ID: {}", id);
    }

    @Override
    public ArticleVo getArticleDetail(Long id) {
        Article article = getAndCheckArticle(id);

        // 增加浏览量
        baseMapper.incrementViewCount(id);

        return convertToVO(article);
    }

    @Override
    public Page<ArticleListVo> listArticles(ArticleQueryDto request) {
        Page<Article> page = new Page<>(request.getPageNum(), request.getPageSize());

        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();

        // 分类筛选
        if (request.getCategoryId() != null) {
            wrapper.eq(Article::getCategoryId, request.getCategoryId());
        }

        // 标签筛选
        if (request.getTagId() != null) {
            List<Long> articleIds = articleTagMapper.selectArticleIdsByTagId(request.getTagId());
            if (CollectionUtils.isEmpty(articleIds)) {
                return new Page<>(request.getPageNum(), request.getPageSize());
            }
            wrapper.in(Article::getId, articleIds);
        }

        // 作者筛选
        if (request.getAuthorId() != null) {
            wrapper.eq(Article::getAuthorId, request.getAuthorId());
        }

        // 关键词搜索
        if (StringUtils.hasText(request.getKeyword())) {
            wrapper.and(w -> w
                    .like(Article::getTitle, request.getKeyword())
                    .or()
                    .like(Article::getSummary, request.getKeyword())
            );
        }

        // 状态筛选
        if (request.getStatus() != null) {
            wrapper.eq(Article::getStatus, request.getStatus());
        } else {
            // 默认只显示已发布的文章
            wrapper.eq(Article::getStatus, 1);
        }

        // 置顶筛选
        if (request.getIsTop() != null) {
            wrapper.eq(Article::getIsTop, request.getIsTop());
        }

        // 精选筛选
        if (request.getIsFeatured() != null) {
            wrapper.eq(Article::getIsFeatured, request.getIsFeatured());
        }

        // 排序：置顶优先，然后按指定字段排序
        wrapper.orderByDesc(Article::getIsTop);
        SFunction<Article, ?> orderColumnFn = switch (request.getOrderBy()) {
            case "publishTime" -> Article::getPublishTime;
            case "viewCount" -> Article::getViewCount;
            case "likeCount" -> Article::getLikeCount;
            default -> Article::getCreateTime;
        };
        wrapper.orderBy(true, request.getAsc(), orderColumnFn);

        Page<Article> articlePage = page(page, wrapper);

        // 转换为VO
        Page<ArticleListVo> voPage = new Page<>(articlePage.getCurrent(), articlePage.getSize(), articlePage.getTotal());
        voPage.setRecords(articlePage.getRecords().stream()
                .map(this::convertToListVO)
                .toList());

        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishDraft(Long id, Long userId, boolean isAdmin) {
        Article article = getAndCheckArticle(id);

        // 权限检查
        checkPermission(article, userId, isAdmin);

        if (article.getStatus() == 1) {
            throw new BusinessException("文章已发布");
        }

        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getId, id)
                .set(Article::getStatus, 1)
                .set(Article::getPublishTime, article.getPublishTime() == null ? LocalDateTime.now() : article.getPublishTime());

        update(wrapper);
        log.info("发布文章成功，文章ID: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void offlineArticle(Long id, Long userId, boolean isAdmin) {
        Article article = getAndCheckArticle(id);

        // 权限检查
        checkPermission(article, userId, isAdmin);

        if (article.getStatus() != 1) {
            throw new BusinessException("只能下架已发布的文章");
        }

        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getId, id)
                .set(Article::getStatus, 2);

        update(wrapper);
        log.info("下架文章成功，文章ID: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setTop(Long id, Integer isTop) {
        Article article = getAndCheckArticle(id);

        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getId, id)
                .set(Article::getIsTop, isTop);

        update(wrapper);
        log.info("设置文章置顶状态，文章ID: {}, 是否置顶: {}", id, isTop);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setFeatured(Long id, Integer isFeatured) {
        Article article = getAndCheckArticle(id);

        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getId, id)
                .set(Article::getIsFeatured, isFeatured);

        update(wrapper);
        log.info("设置文章精选状态，文章ID: {}, 是否精选: {}", id, isFeatured);
    }

    /**
     * 获取文章并检查是否存在
     */
    private Article getAndCheckArticle(Long id) {
        Article article = getById(id);
        if (article == null) {
            throw new BusinessException(ResultCodeEnum.DATA_NOT_EXIST);
        }
        return article;
    }

    /**
     * 检查操作权限
     */
    private void checkPermission(Article article, Long userId, boolean isAdmin) {
        if (!isAdmin && !article.getAuthorId().equals(userId)) {
            throw new BusinessException(ResultCodeEnum.FORBIDDEN);
        }
    }

    /**
     * 保存文章标签关联
     */
    private void saveArticleTags(Long articleId, List<Long> tagIds) {
        for (Long tagId : tagIds) {
            // 验证标签是否存在
            Tag tag = tagMapper.selectById(tagId);
            if (tag != null && tag.getStatus() == 1) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
                articleTag.setTagId(tagId);
                articleTag.setCreateTime(LocalDateTime.now());
                articleTagMapper.insert(articleTag);
            }
        }
    }

    /**
     * 更新分类文章数
     */
    private void updateCategoryArticleCount(Long categoryId, int delta) {
        Category category = categoryMapper.selectById(categoryId);
        if (category != null) {
            LambdaUpdateWrapper<Category> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Category::getId, categoryId)
                    .set(Category::getArticleCount, Math.max(0, (category.getArticleCount() == null ? 0 : category.getArticleCount()) + delta));
            categoryMapper.update(null, wrapper);
        }
    }

    /**
     * 转换为详情VO
     */
    private ArticleVo convertToVO(Article article) {
        ArticleVo vo = new ArticleVo();
        BeanUtils.copyProperties(article, vo);

        // 设置分类名称
        if (article.getCategoryId() != null) {
            Category category = categoryMapper.selectById(article.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }

        // 设置标签信息
        List<Long> tagIds = articleTagMapper.selectTagIdsByArticleId(article.getId());
        vo.setTagIds(tagIds);
        if (!CollectionUtils.isEmpty(tagIds)) {
            List<String> tagNames = new ArrayList<>();
            for (Long tagId : tagIds) {
                Tag tag = tagMapper.selectById(tagId);
                if (tag != null) {
                    tagNames.add(tag.getName());
                }
            }
            vo.setTagNames(tagNames);
        }

        // 设置作者信息
        if (article.getAuthorId() != null) {
            User author = userMapper.selectById(article.getAuthorId());
            if (author != null) {
                vo.setAuthorName(author.getNickname() != null ? author.getNickname() : author.getUsername());
                vo.setAuthorAvatar(author.getAvatar());
            }
        }

        return vo;
    }

    /**
     * 转换为列表VO
     */
    private ArticleListVo convertToListVO(Article article) {
        ArticleListVo vo = new ArticleListVo();
        BeanUtils.copyProperties(article, vo);

        // 设置分类名称
        if (article.getCategoryId() != null) {
            Category category = categoryMapper.selectById(article.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }

        // 设置标签信息
        List<Long> tagIds = articleTagMapper.selectTagIdsByArticleId(article.getId());
        vo.setTagIds(tagIds);
        if (!CollectionUtils.isEmpty(tagIds)) {
            List<String> tagNames = new ArrayList<>();
            for (Long tagId : tagIds) {
                Tag tag = tagMapper.selectById(tagId);
                if (tag != null) {
                    tagNames.add(tag.getName());
                }
            }
            vo.setTagNames(tagNames);
        }

        // 设置作者信息
        if (article.getAuthorId() != null) {
            User author = userMapper.selectById(article.getAuthorId());
            if (author != null) {
                vo.setAuthorName(author.getNickname() != null ? author.getNickname() : author.getUsername());
                vo.setAuthorAvatar(author.getAvatar());
            }
        }

        return vo;
    }
}
