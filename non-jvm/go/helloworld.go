package main

import (
    "io"
    "log"
    "net/http"
)

var iCnt int = 0;

func helloHandler(w http.ResponseWriter, r * http.Request) {
    io.WriteString(w, "hello world")
}

func main() {
    ht := http.HandlerFunc(helloHandler)
    if ht != nil {
        http.Handle("/hello", ht)
    }
    err := http.ListenAndServe(":8090", nil)
    if err != nil {
        log.Fatal("ListenAndServe: ", err.Error())
    }
}
