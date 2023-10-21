package vo

import "go-crud-demo/src/entitys"

type ListVo struct {
	List  []entitys.List `json:"list"`
	Total int64          `json:"total"`
}
