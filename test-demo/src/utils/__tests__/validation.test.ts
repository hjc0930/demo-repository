import { describe, it, expect } from 'vitest'
import {
  isValidEmail,
  isValidPhone,
  isValidIdCard,
  isValidUsername,
  isValidPassword,
  isValidUrl,
  isInRange,
} from '../validation'

describe('验证工具函数', () => {
  describe('isValidEmail', () => {
    it('应该验证有效的邮箱地址', () => {
      expect(isValidEmail('test@example.com')).toBe(true)
      expect(isValidEmail('user.name@domain.co.uk')).toBe(true)
      expect(isValidEmail('user+tag@example.com')).toBe(true)
    })

    it('应该拒绝无效的邮箱地址', () => {
      expect(isValidEmail('invalid')).toBe(false)
      expect(isValidEmail('@example.com')).toBe(false)
      expect(isValidEmail('user@')).toBe(false)
      expect(isValidEmail('user@.com')).toBe(false)
      expect(isValidEmail('')).toBe(false)
    })
  })

  describe('isValidPhone', () => {
    it('应该验证有效的中国手机号', () => {
      expect(isValidPhone('13812345678')).toBe(true)
      expect(isValidPhone('15912345678')).toBe(true)
      expect(isValidPhone('18612345678')).toBe(true)
      expect(isValidPhone('19112345678')).toBe(true)
    })

    it('应该拒绝无效的手机号', () => {
      expect(isValidPhone('12345678901')).toBe(false) // 1开头
      expect(isValidPhone('1381234567')).toBe(false) // 10位
      expect(isValidPhone('138123456789')).toBe(false) // 12位
      expect(isValidPhone('1381234567a')).toBe(false) // 含字母
      expect(isValidPhone('')).toBe(false)
    })
  })

  describe('isValidIdCard', () => {
    it('应该验证有效的身份证号', () => {
      expect(isValidIdCard('110101199001011234')).toBe(true)
      expect(isValidIdCard('31010120000101123X')).toBe(true)
    })

    it('应该拒绝无效的身份证号', () => {
      expect(isValidIdCard('123456')).toBe(false)
      expect(isValidIdCard('12345678901234567')).toBe(false)
      expect(isValidIdCard('')).toBe(false)
    })
  })

  describe('isValidUsername', () => {
    it('应该验证有效的用户名', () => {
      expect(isValidUsername('user123')).toBe(true)
      expect(isValidUsername('test_user')).toBe(true)
      expect(isValidUsername('abc')).toBe(true)
      expect(isValidUsername('a1234567890123456789')).toBe(true)
    })

    it('应该拒绝无效的用户名', () => {
      expect(isValidUsername('ab')).toBe(false) // 太短
      expect(isValidUsername('user name')).toBe(false) // 含空格
      expect(isValidUsername('user-name')).toBe(false) // 含连字符
      expect(isValidUsername('')).toBe(false)
    })
  })

  describe('isValidPassword', () => {
    it('应该验证有效的密码', () => {
      expect(isValidPassword('123456')).toBe(true)
      expect(isValidPassword('password')).toBe(true)
      expect(isValidPassword('abc123')).toBe(true)
    })

    it('应该拒绝无效的密码', () => {
      expect(isValidPassword('12345')).toBe(false) // 5个字符
      expect(isValidPassword('')).toBe(false)
    })
  })

  describe('isValidUrl', () => {
    it('应该验证有效的 URL', () => {
      expect(isValidUrl('https://example.com')).toBe(true)
      expect(isValidUrl('http://example.com')).toBe(true)
      expect(isValidUrl('https://example.com/path')).toBe(true)
      expect(isValidUrl('ftp://example.com')).toBe(true)
    })

    it('应该拒绝无效的 URL', () => {
      expect(isValidUrl('not a url')).toBe(false)
      expect(isValidUrl('example.com')).toBe(false) // 缺少协议
      expect(isValidUrl('')).toBe(false)
    })
  })

  describe('isInRange', () => {
    it('应该验证数字在范围内', () => {
      expect(isInRange(5, 1, 10)).toBe(true)
      expect(isInRange(1, 1, 10)).toBe(true)
      expect(isInRange(10, 1, 10)).toBe(true)
    })

    it('应该拒绝超出范围的数字', () => {
      expect(isInRange(0, 1, 10)).toBe(false)
      expect(isInRange(11, 1, 10)).toBe(false)
    })

    it('应该支持负数范围', () => {
      expect(isInRange(-5, -10, 10)).toBe(true)
      expect(isInRange(0, -10, 10)).toBe(true)
    })
  })
})
