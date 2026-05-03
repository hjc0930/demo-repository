package model

// Result is the unified API response wrapper, compatible with the Java Result<T>.
type Result struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
	Data    any    `json:"data,omitempty"`
}

// PageResult represents a paginated response.
type PageResult struct {
	List     any `json:"list"`
	Total    int64 `json:"total"`
	Page     int   `json:"page"`
	PageSize int   `json:"pageSize"`
}

// Success returns a Result with code 200.
func Success(data any) Result {
	return Result{Code: 200, Message: "success", Data: data}
}

// SuccessNoData returns a Result with code 200 and no data.
func SuccessNoData() Result {
	return Result{Code: 200, Message: "success"}
}

// Error returns a Result with the given code and message.
func Error(code int, message string) Result {
	return Result{Code: code, Message: message}
}
