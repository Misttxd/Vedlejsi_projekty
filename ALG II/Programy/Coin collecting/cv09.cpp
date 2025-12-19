#include <iostream>
#include <vector>
#include <fstream>
#include <sstream>
#include <algorithm>

using std::vector, std::string, std::cout, std::endl, std::max;

using Vector2D = vector<vector<int>>;

Vector2D readFromFile(const string &filename)
{
    std::ifstream file(filename);
    std::vector<std::vector<int>> grid; 

    if (!file.is_open())
    {
        std::cerr << "Unable to open file: " << filename << std::endl;
        return grid;
    }

    string line;

    while (std::getline(file, line))
    {
        std::stringstream lineAsStream(line);
        vector<int> row;

        int num;
        while (lineAsStream >> num)
        {
            row.push_back(num);
        }

        if (!row.empty())
        {
            grid.push_back(row);
        }
    }

    file.close();
    return grid;
}

int main( [[maybe_unused]] int argc, char *argv[])
{
    string filename = argv[1];

    Vector2D C = readFromFile(filename);

    int n = C.size(); // Počet řádků
        
    int m = C[0].size(); // Počet sloupců

    Vector2D F(n, vector<int>(m));

    
    F[0][0] = C[0][0]; // Startovní bod (levý horní roh)

    // Vyplnění prvního řádku
    for (int j = 1; j < m; j++)
    {
        F[0][j] = F[0][j - 1] + C[0][j];
    }

    // Vyplnění prvního sloupce
    for (int i = 1; i < n; i++)
    {
        F[i][0] = F[i - 1][0] + C[i][0];
    }

    // Vyplnění zbytku tabulky
    for (int i = 1; i < n; i++)
    {
        for (int j = 1; j < m; j++)
        {
            F[i][j] = max(F[i - 1][j], F[i][j - 1]) + C[i][j];         
        }
    }

    cout << F[n - 1][m - 1] << endl;

    return 0;
}