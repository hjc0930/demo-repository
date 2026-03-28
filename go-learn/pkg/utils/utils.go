// Package utils 提供通用工具函数
// ============================================================
// 工具函数
// ------------------------------------------------------------
// 放置可复用的通用函数
// 注意: 工具函数应该是无状态的
// ============================================================
package utils

import (
	"crypto/rand"
	"encoding/hex"
	"fmt"
	"regexp"
	"strings"
	"time"
)

// ============================================================
// 字符串工具
// ============================================================

// Truncate 截断字符串到指定长度
func Truncate(s string, maxLen int) string {
	if len(s) <= maxLen {
		return s
	}
	if maxLen <= 3 {
		return s[:maxLen]
	}
	return s[:maxLen-3] + "..."
}

// PadRight 在字符串右侧填充字符
func PadRight(s string, padChar string, length int) string {
	if len(s) >= length {
		return s
	}
	return s + strings.Repeat(padChar, length-len(s))
}

// PadLeft 在字符串左侧填充字符
func PadLeft(s string, padChar string, length int) string {
	if len(s) >= length {
		return s
	}
	return strings.Repeat(padChar, length-len(s)) + s
}

// IsEmpty 检查字符串是否为空（包括空白字符）
func IsEmpty(s string) bool {
	return strings.TrimSpace(s) == ""
}

// ============================================================
// 验证工具
// ============================================================

// IsValidEmail 简单的邮箱验证
// 注意: 生产环境应使用更严格的验证
func IsValidEmail(email string) bool {
	if email == "" {
		return false
	}
	// 简单的正则验证
	pattern := `^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$`
	matched, _ := regexp.MatchString(pattern, email)
	return matched
}

// IsValidPriority 验证优先级
func IsValidPriority(p string) bool {
	switch strings.ToLower(p) {
	case "low", "medium", "high":
		return true
	default:
		return false
	}
}

// ============================================================
// 时间工具
// ============================================================

// FormatDuration 格式化持续时间
func FormatDuration(d time.Duration) string {
	if d < time.Second {
		return fmt.Sprintf("%dms", d.Milliseconds())
	}
	if d < time.Minute {
		return fmt.Sprintf("%.1fs", d.Seconds())
	}
	if d < time.Hour {
		return fmt.Sprintf("%.1fm", d.Minutes())
	}
	return fmt.Sprintf("%.1fh", d.Hours())
}

// StartOfDay 返回一天的开始时间 (00:00:00)
func StartOfDay(t time.Time) time.Time {
	return time.Date(t.Year(), t.Month(), t.Day(), 0, 0, 0, 0, t.Location())
}

// EndOfDay 返回一天的结束时间 (23:59:59)
func EndOfDay(t time.Time) time.Time {
	return time.Date(t.Year(), t.Month(), t.Day(), 23, 59, 59, 999999999, t.Location())
}

// ============================================================
// ID 生成
// ============================================================

// GenerateID 生成随机 ID
func GenerateID(length int) (string, error) {
	bytes := make([]byte, length)
	if _, err := rand.Read(bytes); err != nil {
		return "", err
	}
	return hex.EncodeToString(bytes)[:length], nil
}

// ============================================================
// 切片工具
// ============================================================

// Contains 检查切片是否包含元素
func Contains[T comparable](slice []T, item T) bool {
	for _, v := range slice {
		if v == item {
			return true
		}
	}
	return false
}

// Unique 去重
func Unique[T comparable](slice []T) []T {
	seen := make(map[T]bool)
	result := make([]T, 0)

	for _, v := range slice {
		if !seen[v] {
			seen[v] = true
			result = append(result, v)
		}
	}

	return result
}

// Filter 过滤切片
func Filter[T any](slice []T, predicate func(T) bool) []T {
	result := make([]T, 0)
	for _, v := range slice {
		if predicate(v) {
			result = append(result, v)
		}
	}
	return result
}

// Map 映射切片
func Map[T any, U any](slice []T, transform func(T) U) []U {
	result := make([]U, len(slice))
	for i, v := range slice {
		result[i] = transform(v)
	}
	return result
}

// ============================================================
// Map 工具
// ============================================================

// Keys 获取 map 的所有 key
func Keys[K comparable, V any](m map[K]V) []K {
	keys := make([]K, 0, len(m))
	for k := range m {
		keys = append(keys, k)
	}
	return keys
}

// Values 获取 map 的所有 value
func Values[K comparable, V any](m map[K]V) []V {
	values := make([]V, 0, len(m))
	for _, v := range m {
		values = append(values, v)
	}
	return values
}
