package vo

import "go-crud-demo/src/entitys"

type StudentVo struct {
	List  []entitys.Student `json:"list"`
	Total int64             `json:"total"`
}
