#include <vector>
#include <exception>
#include <iostream>
#include <algorithm>
#include <fstream>
#include <sstream>

using namespace std;

class Heap
{
private:
    vector<int> data;
    int n;

    size_t parent(size_t index) const
    {
        if (index == 0)
        {
            return 0;
        } // note that heap root has no parent
        return (index - 1) / n;
    }

    void heapify(size_t index)
    {
        size_t largest = index;

        for (int k = 1; k <= n; ++k)
        {
            size_t children = n * index + k;
            if (children >= data.size())
            {
                break;
            }

            if (data[children] > data[largest])
            {
                largest = children;
            }
        }

        if (largest != index)
        {
            std::swap(data[index], data[largest]);
            heapify(largest); // restore heap property on subtree
        }
    }

    void makeHeap()
    {
        // build heap from bottom to top
        for (int i = data.size() / 2 - 1; i >= 0; i--)
        {
            heapify(i);
        }
    }

public:
    Heap(int n_ = 2) : n(n_)
    {
    }
    Heap(const vector<int> &input, int n_ = 2) : data(input), n(n_)
    {
        makeHeap();
    }

    Heap(vector<int> &&input, int n_ = 2) : data(std::move(input)), n(n_)
    {
        makeHeap();
    }

    void insert(const int value)
    {
        size_t currentIndex = data.size();
        data.push_back(value);
        size_t parentIndex = parent(currentIndex);

        // repair heap property by pushing the new number up
        while (currentIndex != 0 && data[currentIndex] > data[parentIndex])
        {
            std::swap(data[currentIndex], data[parentIndex]);
            currentIndex = parentIndex;
            parentIndex = parent(currentIndex);
        }
    }

    int getMax()
    {
        if (data.empty())
        {
            throw std::out_of_range("Trying to extract item from empty heap");
        }
        int maxValue = data[0];

        data[0] = data.back();
        data.pop_back();
        if (data.size() > 1)
        {
            heapify(0);
        }

        return maxValue;
    }

    void print() const
    {
        for (const int item : data)
        {
            std::cout << item << " ";
        }
        std::cout << "\n";
    }

    void clear()
    {
        data.clear();
    }
};

void test()
{
    vector<int> data = {10, 11, 1, 2, 3, 100, 200};
    Heap heap;
    for (const int item : data)
    {
        heap.insert(item);
    }
    Heap heap2(std::move(data));
    std::cout << "Note that these constructions do not lead to same internal vector.\n";
    heap2.print();
    heap.print();
}

std::vector<int> readIntegersFromFile(const std::string &filename)
{
    std::ifstream file(filename);
    std::vector<int> numbersVec;

    if (!file.is_open())
    {
        std::cerr << "Unable to open file: " << filename << std::endl;
        return numbersVec;
    }

    std::string line;

    // change if to while to read the whole file
    if (std::getline(file, line))
    {
        std::stringstream lineAsStream(line);
        int num;
        while (lineAsStream >> num)
        { // NOTE: this assumes that the input file has the data we want
            numbersVec.push_back(num);
        }
    }

    file.close();
    return numbersVec;
}

int main([[maybe_unused]] int argc, [[maybe_unused]] char *argv[])
{
    int n = atoi(argv[1]);
    string filename = argv[2];

    vector<int> numbers = readIntegersFromFile(filename);

    Heap heap(numbers, n);
    heap.print();

    heap.getMax();
    heap.print();

    heap.insert(42);
    heap.print();

    heap.insert(-5);
    heap.print();

    heap.getMax();
    heap.print();

    return 0;
}