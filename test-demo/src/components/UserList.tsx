import { useEffect, useState } from 'react'
import { Table, Card, Tag, Space, Button, message, Popconfirm } from 'antd'
import type { ColumnsType, TablePaginationConfig } from 'antd/es/table'
import type { User } from '../types'
import { getUserList } from '../services/userService'

interface UserListProps {
  onEdit?: (user: User) => void
  refreshTrigger?: number
}

export const UserList: React.FC<UserListProps> = ({ onEdit, refreshTrigger }) => {
  const [loading, setLoading] = useState(false)
  const [users, setUsers] = useState<User[]>([])
  const [pagination, setPagination] = useState({ current: 1, pageSize: 10, total: 0 })

  const fetchUsers = async () => {
    try {
      setLoading(true)
      const response = await getUserList() as any
      setUsers(response.data || [])
      setPagination(prev => ({ ...prev, total: response.data?.length || 0 }))
    } catch (error) {
      message.error('获取用户列表失败')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchUsers()
  }, [refreshTrigger])

  const handleTableChange = (newPagination: TablePaginationConfig) => {
    setPagination({
      current: newPagination.current || 1,
      pageSize: newPagination.pageSize || 10,
      total: pagination.total,
    })
  }

  const handleDelete = (id: number) => {
    setUsers(prev => prev.filter(user => user.id !== id))
    message.success('删除成功')
  }

  const columns: ColumnsType<User> = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 80,
    },
    {
      title: '用户名',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: '角色',
      dataIndex: 'role',
      key: 'role',
      render: (role: string) => (
        <Tag color={role === 'admin' ? 'red' : 'blue'}>
          {role === 'admin' ? '管理员' : '普通用户'}
        </Tag>
      ),
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      key: 'createdAt',
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => onEdit?.(record)}>
            编辑
          </Button>
          <Popconfirm
            title="确定要删除这个用户吗?"
            onConfirm={() => handleDelete(record.id)}
            okText="确定"
            cancelText="取消"
          >
            <Button type="link" danger>
              删除
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ]

  return (
    <Card title="用户列表">
      <Table
        columns={columns}
        dataSource={users}
        rowKey="id"
        loading={loading}
        pagination={pagination}
        onChange={handleTableChange}
      />
    </Card>
  )
}

export default UserList
