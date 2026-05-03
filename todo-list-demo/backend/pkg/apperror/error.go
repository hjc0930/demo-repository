package apperror

// BusinessError represents a business-level error with code and message.
type BusinessError struct {
	Code    int
	Message string
}

func (e *BusinessError) Error() string {
	return e.Message
}

// New creates a BusinessError with default code 400.
func New(message string) *BusinessError {
	return &BusinessError{Code: 400, Message: message}
}

// NewWithCode creates a BusinessError with a custom code.
func NewWithCode(code int, message string) *BusinessError {
	return &BusinessError{Code: code, Message: message}
}
