#include <vector>
#include <iostream>
#include <algorithm>
#include <fstream>
#include <sstream>
#include <set>
#include <map>

using namespace std;

using Simplex = vector<int>;

std::vector<Simplex> readIntegersFromFile(const std::string &filename)
{
    std::ifstream file(filename);
    std::vector<Simplex> numbersSimplex;
    if (!file.is_open())
    {
        std::cerr << "Unable to open file: " << filename << std::endl;
        return numbersSimplex;
    }
    std::string line;
    // change if to while to read the whole file

    while (std::getline(file, line))
    {
        Simplex TMPsimplex;
        std::stringstream lineAsStream(line);
        int num;
        while (lineAsStream >> num)
        { // NOTE: this assumes that the input file has the data we want
            TMPsimplex.push_back(num);
        }

        sort(TMPsimplex.begin(), TMPsimplex.end());
        numbersSimplex.push_back(TMPsimplex);
    }
    file.close();
    return numbersSimplex;
}


std::vector<Simplex> get_combinations(const Simplex& simplex, int k) {
    std::vector<Simplex> result;
    if (k > simplex.size() || k < 0) return result;

    std::vector<bool> selector(simplex.size());
    std::fill(selector.begin() + k, selector.end(), true);

    do {
        Simplex current_combination;
        for (int i = 0; i < simplex.size(); ++i) {
            if (!selector[i]) {
                current_combination.push_back(simplex[i]);
            }
        }
        result.push_back(current_combination);
    } while (std::next_permutation(selector.begin(), selector.end()));

    return result;
}


int main([[maybe_unused]] int argc, [[maybe_unused]] char *argv[])
{
    string filename = argv[1];
    vector<Simplex> numbers = readIntegersFromFile(filename);

    if (numbers.empty()) {
        cout << "Input file is empty or could not be read." << endl;
        return 0;
    }

    std::set<int> unique_vertices;
    std::map<Simplex, int> subsimplex_counts;

    for (const auto& simplex : numbers) {
        for (int vertex : simplex) {
            unique_vertices.insert(vertex);
        }

        if (simplex.size() > 1) {
            for (const auto& subsimplex : get_combinations(simplex, simplex.size() - 1)) {
                subsimplex_counts[subsimplex]++;
            }
        }
    }

    int k0 = 0, k1 = 0, k2 = 0, k3 = 0;

    std::set<Simplex> unique_edges_set;
    std::set<Simplex> unique_triangles_set;
    std::set<Simplex> unique_tetrahedrons_set;

    for (const auto& simplex : numbers) {
        if (simplex.size() >= 2) {
            for (const auto& edge : get_combinations(simplex, 2)) {
                unique_edges_set.insert(edge);
            }
        }
        if (simplex.size() >= 3) {
            for (const auto& triangle : get_combinations(simplex, 3)) {
                unique_triangles_set.insert(triangle);
            }
        }
        if (simplex.size() >= 4) {
            for (const auto& tetra : get_combinations(simplex, 4)) {
                unique_tetrahedrons_set.insert(tetra);
            }
        }
    }

    k0 = unique_vertices.size();
    k1 = unique_edges_set.size();
    k2 = unique_triangles_set.size();
    k3 = unique_tetrahedrons_set.size();

    cout << "Verticies: " << k0 << endl;
    if (k1 > 0) cout << "Edges: " << k1 << endl;
    if (k2 > 0) cout << "Triangles: " << k2 << endl;
    if (k3 > 0) cout << "Tetrahedrons: " << k3 << endl;

    int chi = k0 - k1 + k2 - k3;
    cout << "chi: " << chi << endl << endl;

    vector<Simplex> boundary;
    for (const auto& pair : subsimplex_counts) {
        if (pair.second == 1) {
            boundary.push_back(pair.first);
        }
    }
    sort(boundary.begin(), boundary.end());

    
    if (boundary.empty()) {
        cout << "Boundary:" << endl;
        cout << "is empty" << endl;
    } else {
        
        for (const auto& b_simplex : boundary) {
            for (size_t i = 0; i < b_simplex.size(); ++i) {
                cout << b_simplex[i] << (i == b_simplex.size() - 1 ? "" : " ");
            }
            cout << endl;
        }
    }

    return 0;
}