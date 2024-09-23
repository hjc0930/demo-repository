package main

import (
	"fmt"
	"runtime"
)

type MemberInfo struct {
	Total     uint64
	Free      uint64
	Available uint64
}

func getMemberInfo() MemberInfo {
	var memberInfo runtime.MemStats

	runtime.ReadMemStats(&memberInfo)

	return MemberInfo{
		Total:     memberInfo.TotalAlloc,
		Free:      memberInfo.Frees,
		Available: memberInfo.Alloc,
	}

}

func main() {
	memberInfo := getMemberInfo()

	fmt.Println("Total Memory Allocated:", memberInfo.Total/1024)
	fmt.Println("Total Memory Free:", memberInfo.Free/1024)
	fmt.Println("Total Memory Available:", memberInfo.Available/1024)
}
