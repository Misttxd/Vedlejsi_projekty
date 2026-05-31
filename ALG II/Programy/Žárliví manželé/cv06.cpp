#include <vector>
#include <set>
#include <iostream>
#include <queue>
#include <string>
#include <map>
#include <algorithm> 

using namespace std;

class State 
{
private:
    set<string> finalState;

    bool isValid(const set<string>& banka) const 
    {
        set<string> muz, zena;
        for (const string& clovek : banka) 
        {
            if (clovek[0] == 'M') 
            {
                muz.insert(clovek);
            }
            else 
            {
                zena.insert(clovek);
            }
        }

        if (muz.empty() || zena.empty()) 
        {
            return true;
        }

        for (const string& w : zena) 
        {
            char id = w[1];
            string h = "M";
            h += id;

            if (banka.count(h) > 0) 
            {
                continue;
            }
            
            for (const string& m : muz) 
            {
                char his_id = m[1];
                string his_w = "Z";
                his_w += his_id;

                if (banka.count(his_w) == 0) 
                {
                    return false;
                }
            }
        }
        return true;
    }

public:
    set<string> banka1, banka2;
    bool lodB1;
    int num_parky;

    State(int n_parky = 3) 
    {
        num_parky = n_parky;
        lodB1 = true;
        for (int i = 0; i < n_parky; ++i) 
        {
            string p_str = std::to_string(i);
            banka1.insert("M" + p_str);
            banka1.insert("Z" + p_str);
            finalState.insert("M" + p_str);
            finalState.insert("Z" + p_str);
        }
    }
    
    State(const set<string>& b1, const set<string>& b2, bool boatPos, int n, const set<string>& finalS)
    {
         banka1 = b1;
         banka2 = b2;
         lodB1 = boatPos;
         num_parky = n;
         finalState = finalS;
    }

    bool operator<(const State& other) const 
    {
        if (banka1 != other.banka1) 
        {
            return banka1 < other.banka1;
        }
        if (banka2 != other.banka2) 
        {
            return banka2 < other.banka2;
        }
        return lodB1 < other.lodB1;
    }

    bool isFinal() const 
    {
        return banka2 == finalState && lodB1 == false;
    }

    vector<State> generateChildren() const 
    {
        vector<State> res;
        
        const set<string>* activebanka;
        const set<string>* targetbanka;

        if (lodB1 == true) 
        {
            activebanka = &banka1;
            targetbanka = &banka2;
        } 
        else 
        {
            activebanka = &banka2;
            targetbanka = &banka1;
        }

        vector<string> lidi(activebanka->begin(), activebanka->end());
        size_t n = lidi.size();

        for (size_t i = 0; i < n; ++i) 
        {
            set<string> boat = { lidi[i] };
            set<string> proposedA = *activebanka;
            set<string> proposedT = *targetbanka;
            proposedA.erase(lidi[i]);
            proposedT.insert(lidi[i]);

            if (isValid(proposedA) && isValid(proposedT)) 
            {
                if (lodB1) 
                {
                    res.emplace_back(proposedA, proposedT, !lodB1, num_parky, finalState);
                } 
                else 
                {
                    res.emplace_back(proposedT, proposedA, !lodB1, num_parky, finalState);
                }
            }
        }

        for (size_t i = 0; i < n; ++i) 
        {
            for (size_t j = i + 1; j < n; ++j) 
            {
                set<string> boat = { lidi[i], lidi[j] };
                if (!isValid(boat)) 
                {
                    continue;
                }

                set<string> proposedA = *activebanka;
                set<string> proposedT = *targetbanka;
                proposedA.erase(lidi[i]);
                proposedA.erase(lidi[j]);
                proposedT.insert(lidi[i]);
                proposedT.insert(lidi[j]);

                if (isValid(proposedA) && isValid(proposedT)) 
                {
                    if (lodB1) 
                    {
                        res.emplace_back(proposedA, proposedT, !lodB1, num_parky, finalState);
                    } 
                    else 
                    {
                        res.emplace_back(proposedT, proposedA, !lodB1, num_parky, finalState);
                    }
                }
            }
        }
        return res;
    }

    bool operator==(const State& other) const 
    {
        return (banka1 == other.banka1) && (banka2 == other.banka2) && (lodB1 == other.lodB1);
    }
};

vector<State> solve(int n_parky) 
{
    State start(n_parky);

    map<State, State> child2parentMap;
    queue<State> bfsQ;
    set<State> visited;
    
    bfsQ.push(start);
    child2parentMap[start] = start;
    visited.insert(start);
    bool victoryFlag = false;

    vector<State> path;

    while (!bfsQ.empty() && !victoryFlag) 
    {
        State currentNode = bfsQ.front();
        bfsQ.pop();

        auto children = currentNode.generateChildren();

        for (auto child : children) 
        {
            if (visited.find(child) != visited.end()) 
            {
                continue;
            }
            bfsQ.push(child);
            child2parentMap[child] = currentNode;
            visited.insert(child);
            if (child.isFinal()) 
            {
                path.push_back(child);
                State parent = child2parentMap[child];
                while (!!(child == parent)) 
                {
                    path.push_back(parent);
                    child = parent;
                    parent = child2parentMap[child];
                }
                victoryFlag = true;
                break;
            }
        }
    }
    std::reverse(path.begin(), path.end());
    return path;
}

int main() 
{
    auto res2 = solve(2);
    auto res3 = solve(3);
    auto res4 = solve(4);

    std::cout << "2 pairs - " << res2.size() - 1 << " transports\n";
    std::cout << "3 pairs - " << res3.size() - 1 << " transports\n";
    std::cout << "4 pairs - " << res4.size() - 1 << " transports\n";
    return 0;
}