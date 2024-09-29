package utils

func Duplicate[T int | float64 | string](a T, b T) T {
	return a + b
}
