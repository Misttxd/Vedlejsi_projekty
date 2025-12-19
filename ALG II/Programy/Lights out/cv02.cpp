#include <iostream>
#include <vector>
#include <fstream>
#include <sstream>
#include <algorithm>

using namespace std;

class Z2
{
private:
    int value;

public:
    friend ostream &operator<<(ostream &os, const Z2 &z);

    Z2(int v = 0) : value((v % 2 + 2) % 2) {}

    Z2 operator+(const Z2 &other) const
    {
        return Z2(value + other.value);
    }
    Z2 operator-(const Z2 &other) const
    {
        return Z2(value - other.value);
    }
    Z2 operator*(const Z2 &other) const
    {
        return Z2(value * other.value);
    }
    Z2 operator/(const Z2 &other) const
    {
        return Z2(value);
    }

    Z2 &operator+=(const Z2 &other)
    {
        value = (value + other.value) % 2;
        return *this;
    }
    Z2 &operator-=(const Z2 &other)
    {
        value = (value - other.value + 2) % 2;
        return *this;
    }
    Z2 &operator*=(const Z2 &other)
    {
        value = (value * other.value) % 2;
        return *this;
    }

    bool operator==(const Z2 &other) const
    {
        return value == other.value;
    }
    bool operator!=(const Z2 &other) const
    {
        return value != other.value;
    }
};

ostream &operator<<(ostream &os, const Z2 &z)
{
    os << z.value;
    return os;
}

struct Direction
{
    int dx;
    int dy;
};

vector<vector<Z2>> build_A(int N)
{
    const int M = N * N;
    vector<vector<Z2>> A(M, vector<Z2>(M, Z2(0)));

    const Direction directions[] = {
        {0, 0},
        {-1, 0},
        {1, 0},
        {0, -1},
        {0, 1}};
    const int NUM_DIRECTIONS = 5;

    for (int y = 0; y < N; ++y)
    {
        for (int x = 0; x < N; ++x)
        {

            int button_index = x + N * y;

            for (int k = 0; k < NUM_DIRECTIONS; ++k)
            {

                int dx = directions[k].dx;
                int dy = directions[k].dy;

                int nx = x + dx;
                int ny = y + dy;

                if (nx >= 0 && nx < N && ny >= 0 && ny < N)
                {

                    int affected_index = nx + N * ny;

                    A[affected_index][button_index] = Z2(1);
                }
            }
        }
    }

    return A;
}

int main(int argc, char const *argv[])
{
    const int n = stoi(argv[1]);
    const int M = n * n;

    if (argc != M + 2)
    {
        cerr << "Chyba: Nesprávný počet argumentů\n";
        return 1;
    }

    vector<Z2> b(M);
    for (int i = 0; i < M; ++i)
    {
        b[i] = Z2(stoi(argv[i + 2]));
    }

    vector<vector<Z2>> matice = build_A(n);
    for (int i = 0; i < M; ++i)
    {
        matice[i].push_back(b[i]);
    }

    int pivot_radek = 0;
    for (int j = 0; j < M && pivot_radek < M; ++j)
    {
        int i = pivot_radek;
        while (i < M && matice[i][j] == Z2(0))
        {
            i++;
        }

        if (i < M)
        {
            swap(matice[i], matice[pivot_radek]);

            for (int k = 0; k < M; ++k)
            {
                if (k != pivot_radek && matice[k][j] == Z2(1))
                {
                    for (int l = j; l <= M; ++l)
                    {
                        matice[k][l] += matice[pivot_radek][l];
                    }
                }
            }
            pivot_radek++;
        }
    }

    vector<Z2> x(M, Z2(0));
    for (int i = 0; i < M; ++i)
    {
        bool je_NULA = true;
        int pivot_sloupec = -1;

        for (int j = 0; j < M; ++j)
        {
            if (matice[i][j] != Z2(0))
            {
                je_NULA = false;
                pivot_sloupec = j;
                break;
            }
        }

        if (je_NULA && matice[i][M] == Z2(1))
        {
            return 0;
        }

        if (!je_NULA)
        {
            x[pivot_sloupec] = matice[i][M];
        }
    }

    for (int i = 0; i < M; ++i)
    {
        cout << x[i] << " ";
    }
    cout << "\n";

    return 0;

    return 0;
}