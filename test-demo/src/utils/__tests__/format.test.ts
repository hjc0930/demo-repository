import { describe, it, expect } from 'vitest'
import {
  formatDate,
  formatFileSize,
  formatCurrency,
  formatNumber,
  truncateText,
} from '../format'

describe('格式化工具函数', () => {
  describe('formatDate', () => {
    it('应该格式化日期为默认格式', () => {
      const date = new Date('2024-01-15T10:30:45')
      expect(formatDate(date)).toBe('2024-01-15 10:30:45')
    })

    it('应该支持自定义格式', () => {
      const date = new Date('2024-01-15T10:30:45')
      expect(formatDate(date, 'YYYY-MM-DD')).toBe('2024-01-15')
      expect(formatDate(date, 'YYYY年MM月DD日')).toBe('2024年01月15日')
    })

    it('应该支持字符串日期', () => {
      expect(formatDate('2024-01-15T10:30:45')).toBe('2024-01-15 10:30:45')
    })

    it('应该正确处理个位数的月份和日期', () => {
      const date = new Date('2024-01-05T08:05:09')
      expect(formatDate(date)).toBe('2024-01-05 08:05:09')
    })
  })

  describe('formatFileSize', () => {
    it('应该格式化字节', () => {
      expect(formatFileSize(0)).toBe('0 B')
      expect(formatFileSize(500)).toBe('500 B')
    })

    it('应该格式化千字节', () => {
      expect(formatFileSize(1024)).toBe('1 KB')
      expect(formatFileSize(2048)).toBe('2 KB')
    })

    it('应该格式化兆字节', () => {
      expect(formatFileSize(1024 * 1024)).toBe('1 MB')
      expect(formatFileSize(5 * 1024 * 1024)).toBe('5 MB')
    })

    it('应该格式化吉字节', () => {
      expect(formatFileSize(1024 * 1024 * 1024)).toBe('1 GB')
    })

    it('应该保留两位小数', () => {
      expect(formatFileSize(1536)).toBe('1.5 KB')
      expect(formatFileSize(1024 * 1024 * 1.5)).toBe('1.5 MB')
    })
  })

  describe('formatCurrency', () => {
    it('应该格式化人民币', () => {
      expect(formatCurrency(1234.56)).toBe('¥1,234.56')
    })

    it('应该处理整数', () => {
      expect(formatCurrency(1000)).toBe('¥1,000.00')
    })

    it('应该支持小数', () => {
      expect(formatCurrency(1234.5)).toBe('¥1,234.50')
    })

    it('应该支持自定义货币', () => {
      expect(formatCurrency(100, 'USD', "en-us")).toBe('$100.00')
      expect(formatCurrency(100, 'EUR', 'de-DE')).toBe('100,00 €')
    })
  })

  describe('formatNumber', () => {
    it('应该添加千分位分隔符', () => {
      expect(formatNumber(1000)).toBe('1,000')
      expect(formatNumber(1000000)).toBe('1,000,000')
    })

    it('应该处理小数', () => {
      expect(formatNumber(1234.56)).toBe('1,234.56')
    })

    it('应该处理小数字', () => {
      expect(formatNumber(123)).toBe('123')
    })

    it('应该处理大数字', () => {
      expect(formatNumber(1234567890)).toBe('1,234,567,890')
    })
  })

  describe('truncateText', () => {
    it('应该在超过最大长度时截断文本', () => {
      expect(truncateText('Hello World', 5)).toBe('Hello...')
    })

    it('应该在不超过最大长度时返回原文', () => {
      expect(truncateText('Hi', 5)).toBe('Hi')
      expect(truncateText('Hello', 5)).toBe('Hello')
    })

    it('应该处理空字符串', () => {
      expect(truncateText('', 10)).toBe('')
    })

    it('应该处理正好等于最大长度的文本', () => {
      expect(truncateText('Hello', 5)).toBe('Hello')
    })
  })
})
