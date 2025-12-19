#include <iostream>
#include <vector>
#include <algorithm>
#include <tuple>
#include <unordered_map>
#include <fstream>
#include <sstream>

using std::vector, std::unordered_map, std::get, std::sort, std::cout, std::endl, std::cerr;

using label = int;

// kod ze cviceni
// union by size
class UnionFindStructure
{
private:
    unordered_map<label, label> parent; // element -> parent/representant of element
    unordered_map<label, size_t> sizes; // element -> size of its partition if element is representant, rubish otherwise

public:
    // construct from universe of elements
    UnionFindStructure(const vector<label> &universe)
    {
        for (const auto &el : universe)
        {
            parent[el] = el;
            sizes[el] = 1;
        }
    }
    // construct from number of labels label {0...(n-1)}
    UnionFindStructure(const int n)
    {
        for (int i = 0; i < n; i++)
        {
            parent[i] = i;
            sizes[i] = 1;
        }
    }

    label findRepresentative(label x)
    {
        if (parent[x] != x)
        {
            parent[x] = findRepresentative(parent[x]); // path compression
        }
        return parent[x];
    }

    void setUnion(label x, label y)
    {
        label rootX = findRepresentative(x);
        label rootY = findRepresentative(y);

        if (rootX == rootY)
        {
            return; // already in the same partition
        }

        // ensure rootX represents the bigger set
        if (sizes[rootX] < sizes[rootY])
        {
            std::swap(rootX, rootY);
        }

        // merge smaller set into the larger one
        parent[rootY] = rootX;
        sizes[rootX] += sizes[rootY]; // note that only the representatnt of the subset holds corrent subset size
    }

    bool inTheSameSubset(label x, label y)
    {
        return findRepresentative(x) == findRepresentative(y);
    }
};

using WeightedEdge1 = std::tuple<int, label, label>; // weight, from, to
using Graph = vector<WeightedEdge1>;

// podle pseudokodu
Graph kruskal(int n, Graph &edges)
{
    Graph mst;

    // Sort the edges of G in nondecreasing order of weights:
    sort(edges.begin(), edges.end());

    // Initialize:
    UnionFindStructure uf(n);

    int edge_counter = 0;
    long unsigned int i = 0;

    // While edge_counter < |V| - 1:
    while (edge_counter < n - 1 && i < edges.size())
    {
        const auto &edge = edges[i];
        int weight = get<0>(edge);
        label u = get<1>(edge);
        label v = get<2>(edge);

        if (!uf.inTheSameSubset(u, v))
        {
            mst.push_back(edge);
            uf.setUnion(u, v);
            edge_counter++;
        }
        i++;
    }

    return mst;
}

Graph readFromFile(const std::string &filename, int &n)
{
    std::ifstream file(filename);
    Graph edges;
    n = 0;

    if (!file.is_open())
    {
        std::cerr << "Unable to open file: " << filename << std::endl;
        return edges;
    }

    std::string line;
    int row = 0;

    while (std::getline(file, line))
    {
        std::stringstream lineAsStream(line);
        int weight;
        int col = 0;
        while (lineAsStream >> weight)
        { // NOTE: this assumes that the input file has the data we want
            if (weight > 0 && row < col)
            {
                edges.push_back({weight, row, col});
            }
            col++;
        }
        if (n == 0)
        {
            n = col;
        }
        row++;
    }

    file.close();
    return edges;
}

int main([[maybe_unused]] int argc, char *argv[])
{
    std::string filename = argv[1];
    int n = 0;
    Graph edges = readFromFile(filename, n);

    Graph mst = kruskal(n, edges);

    int total_weight = 0;
    for (size_t i = 0; i < mst.size(); i++)
    {
        total_weight += get<0>(mst[i]);
    }
    cout << total_weight << endl;

    return 0;
}