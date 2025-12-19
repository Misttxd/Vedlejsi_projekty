#include <iostream>
#include <algorithm>
#include <sstream>
#include <fstream>
#include <string>
#include <vector>
#include <queue>

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

    if (std::getline(file, line))
    {
        std::stringstream lineAsStream(line);
        int num;
        while (lineAsStream >> num)
        {
            numbersVec.push_back(num);
        }
    }

    file.close();
    return numbersVec;
}

struct Node
{
    int key;
    Node *left;
    Node *right;
    int heigth;

    Node(int k) : key(k), left(nullptr), right(nullptr), heigth(0) {}
};

class BinaryTree
{
private:
    Node *root;

    int heigth(Node *node)
    {
        if (node == nullptr)
        {
            return -1;
        }

        return node->heigth;
    }

    int balanceFactor(Node *node)
    {
        if (node == nullptr)
        {
            return 0;
        }
        return heigth(node->left) - heigth(node->right);
    }

    void updateHeigth(Node *node)
    {
        if (node == nullptr)
        {
            return;
        }

        node->heigth = 1 + std::max(heigth(node->left), heigth(node->right));
    }

    Node *rotateRight(Node *r)
    {
        Node *c = r->left;
        Node *t2 = c->right;

        c->right = r;
        r->left = t2;

        updateHeigth(r);
        updateHeigth(c);

        return c;
    }

    Node *rotateLeft(Node *r)
    {
        Node *c = r->right;
        Node *t2 = c->left;

        c->left = r;
        r->right = t2;

        updateHeigth(r);
        updateHeigth(c);

        return c;
    }

    Node *balance(Node *node)
    {
        updateHeigth(node);
        int bf = balanceFactor(node);

        if (bf > 1)
        {
            if (balanceFactor(node->left) < 0)
            {
                node->left = rotateLeft(node->left);
            }
            return rotateRight(node);
        }

        if (bf < -1)
        {
            if (balanceFactor(node->right) > 0)
            {
                node->right = rotateRight(node->right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    Node *insert(Node *node, int key)
    {
        if (node == nullptr)
        {
            return new Node(key);
        }
        if (key < node->key)
        {
            node->left = insert(node->left, key);
        }
        else if (key > node->key)
        {
            node->right = insert(node->right, key);
        }
        else
        {
            return node;
        }

        return balance(node);
    }

    bool containsKey(Node *node, int key)
    {
        if (node == nullptr)
            return false;
        if (node->key == key)
        {
            return true;
        }
        if (key < node->key)
        {
            return containsKey(node->left, key);
        }
        else
        {
            return containsKey(node->right, key);
        }
    }

    Node *findMinKeyNode(Node *node)
    {
        while (node->left != nullptr)
        {
            node = node->left;
        }
        return node;
    }

    Node *erase(Node *node, int key)
    {
        if (node == nullptr)
        {
            return nullptr;
        }

        if (key < node->key)
        {
            node->left = erase(node->left, key);
        }
        else if (key > node->key)
        {
            node->right = erase(node->right, key);
        }
        else
        {
            if (node->left == nullptr || node->right == nullptr)
            {
                Node *temp = node->left ? node->left : node->right;
                delete node;
                return temp;
            }

            Node *successor = findMinKeyNode(node->right);
            node->key = successor->key;
            node->right = erase(node->right, successor->key);
        }

        return balance(node);
    }

    void inorderPrint(Node *node) const
    {
        if (node == nullptr)
        {
            return;
        }

        inorderPrint(node->left);
        std::cout << node->key << " ";
        inorderPrint(node->right);
    }

    void preorderPrint(Node *node) const
    {
        if (node == nullptr)
        {
            return;
        }
        std::cout << node->key << " ";
        preorderPrint(node->left);
        preorderPrint(node->right);
    }

    void postorderPrint(Node *node) const
    {
        if (node == nullptr)
        {
            return;
        }
        postorderPrint(node->left);
        postorderPrint(node->right);
        std::cout << node->key << " ";
    }

    void clear(Node *node)
    {
        if (node == nullptr)
        {
            return;
        }
        clear(node->left);
        clear(node->right);
        delete node;
    }

public:
    BinaryTree() : root(nullptr) {}

    ~BinaryTree()
    {
        clear(root);
    }

    void insert(int key)
    {
        root = insert(root, key);
    }

    void erase(int key)
    {
        root = erase(root, key);
    }

    void printPreorder() const
    {
        preorderPrint(root);
        std::cout << "\n";
    }

    void printPostorder() const
    {
        postorderPrint(root);
        std::cout << "\n";
    }

    void printInorder() const
    {
        inorderPrint(root);
        std::cout << "\n";
    }

    void printLevelOrder() const
    {
        if (root == nullptr)
        {
            std::cerr << "Tree is empty\n";
            return;
        }

        std::queue<Node *> q;
        q.push(root);

        while (!q.empty())
        {
            size_t nodeCount = q.size();

            bool foundNewNode = false;
            for (size_t i = 0; i < nodeCount; i++)
            {
                Node *currentNode = q.front();
                q.pop();

                if (currentNode != nullptr)
                {
                    std::cerr << currentNode->key << " ";
                }
                else
                {
                    std::cerr << "# ";
                }
                if (currentNode != nullptr)
                {
                    if (currentNode->left != nullptr || currentNode->right != nullptr)
                    {
                        foundNewNode = true;
                    }
                    q.push(currentNode->left);
                    q.push(currentNode->right);
                }
            }
            std::cerr << "\n";
        }
    }
};

int main(int argc, char *argv[])
{

    std::vector<int> construction = readIntegersFromFile(argv[1]);
    std::vector<int> deletion = readIntegersFromFile(argv[2]);

    BinaryTree tree;

    for (const int item : construction)
    {
        tree.insert(item);
    }

    for (const int item : deletion)
    {
        tree.erase(item);
    }

    tree.printPostorder();
    tree.printPreorder();
    tree.printInorder();

    return 0;
}
