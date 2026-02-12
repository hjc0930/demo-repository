import { useState } from 'react'
import { Form, Input, Button, Card, message } from 'antd'
import type { FormInstance } from 'antd/es/form'

export interface UserFormValues {
  username: string
  email: string
  password?: string
}

interface UserFormProps {
  initialValues?: UserFormValues
  onSubmit: (values: UserFormValues) => Promise<void>
  submitText?: string
  showPassword?: boolean
  formRef?: React.RefObject<FormInstance>
}

export const UserForm: React.FC<UserFormProps> = ({
  initialValues,
  onSubmit,
  submitText = '提交',
  showPassword = true,
  formRef,
}) => {
  const [form] = Form.useForm()
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (values: UserFormValues) => {
    try {
      setLoading(true)
      await onSubmit(values)
      message.success('操作成功')
      if (!initialValues) {
        form.resetFields()
      }
    } catch (error) {
      message.error('操作失败')
    } finally {
      setLoading(false)
    }
  }

  return (
    <Card style={{ maxWidth: 500 }}>
      <Form
        form={form}
        ref={formRef}
        layout="vertical"
        initialValues={initialValues}
        onFinish={handleSubmit}
      >
        <Form.Item
          label="用户名"
          name="username"
          rules={[
            { required: true, message: '请输入用户名' },
            { min: 3, max: 20, message: '用户名长度为3-20个字符' },
            { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线' },
          ]}
        >
          <Input placeholder="请输入用户名" />
        </Form.Item>

        <Form.Item
          label="邮箱"
          name="email"
          rules={[
            { required: true, message: '请输入邮箱' },
            { type: 'email', message: '请输入有效的邮箱地址' },
          ]}
        >
          <Input placeholder="请输入邮箱" />
        </Form.Item>

        {showPassword && (
          <Form.Item
            label="密码"
            name="password"
            rules={[
              { required: !initialValues, message: '请输入密码' },
              { min: 6, message: '密码至少6个字符' },
            ]}
          >
            <Input.Password placeholder="请输入密码" />
          </Form.Item>
        )}

        <Form.Item>
          <Button type="primary" htmlType="submit" loading={loading} block>
            {submitText}
          </Button>
        </Form.Item>
      </Form>
    </Card>
  )
}

export default UserForm
