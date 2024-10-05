package main

// type Shape interface {
// 	Area() float64
// 	Perimeter() float64
// }

// type Rectangle struct {
// 	Width, Height float64
// }

// func (r Rectangle) Area() float64 {
// 	return r.Width * r.Height
// }

// func (r Rectangle) Perimeter() float64 {
// 	return 2 * (r.Width + r.Height)
// }

// var m = sync.Mutex{}
// var wg = sync.WaitGroup{}
// var dbData = []string{"id1", "id2", "id3", "id4", "id5"}
// var results = []string{}

func main() {
	// var c = make(chan int, 5)

	// go process(c)
	// for i := range c {
	// 	fmt.Println(i)
	// 	time.Sleep(time.Second * 1)
	// }
	// t0 := time.Now()

	// for i := 0; i < len(dbData); i++ {
	// 	wg.Add(1)
	// 	go dbCall(i)
	// }
	// wg.Wait()
	// fmt.Printf("\nTotal execution time: %v", time.Since(t0))
	// fmt.Printf("\nThe results are: %v", results)
}

// func process(c chan int) {
// 	defer close(c)
// 	for i := 0; i < 5; i++ {
// 		c <- i
// 	}
// }

// func dbCall(i int) {
// 	var delay float32 = rand.Float32() * 2000

// 	time.Sleep(time.Duration(delay) * time.Millisecond)
// 	save(dbData[i])
// 	log()
// 	wg.Done()
// }

// func save(result string) {
// 	m.Lock()
// 	results = append(results, result)
// 	m.Unlock()
// }

// func log() {
// 	m.Lock()
// 	fmt.Printf("\nThe current results are: %v", results)
// 	m.Unlock()
// }
