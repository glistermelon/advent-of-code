#pragma once

#include <cstring>
#include <optional>

template <class T>
class Array2D {

protected:

    T* arr;
    size_t w;
    size_t h;

public:

    // class Iterator {

    //     T* ptr;

    // public:

    //     T value;
    //     size_t y, x;
    //     const size_t map_w;

    //     Iterator(T* ptr, size_t y, size_t x, size_t map_w) : ptr(ptr), y(y), x(x), map_w(map_w) { }

    //     Iterator& operator++() {

    //         ++ptr;

    //         if (++x == map_w) {
    //             x = 0;
    //             ++y;
    //         }

    //         return *this;
            
    //     }

    //     Iterator& operator*() {
    //         return *this;
    //     }

    //     bool operator==(const Iterator& other) const {
    //         return ptr == other.ptr;
    //     }

    // };

    Array2D() : arr(nullptr) { }

    Array2D(size_t h, size_t w) : h(h), w(w) {
        const auto size = w * h;
        arr = new T[size];
        memset(arr, 0, size * sizeof(T));
    }

    T* operator[](size_t y) {
        return reinterpret_cast<T*>(arr + w * y);
    }

    const T* operator[](size_t y) const {
        return reinterpret_cast<const T*>(arr + w * y);
    }

    Array2D<T>& operator=(const Array2D<T>& other) {
        arr = other.arr;
        w = other.w;
        h = other.h;
        return *this;
    }

    // Iterator begin() {
    //     return Iterator(arr, 0, 0, map_w);
    // }

    // Iterator end() {
    //     return Iterator(arr + w * h, map_h, map_w, map_w);
    // }

    T* begin() {
        return arr;
    }

    T* end() {
        return arr + w * h;
    }

    size_t height() {
        return h;
    }

    size_t width() {
        return w;
    }

};