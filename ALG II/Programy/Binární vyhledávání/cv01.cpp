#include <iostream>
#include <vector>
#include <fstream>
#include <sstream>
#include <algorithm>

using namespace std;

vector<int> readIntsFromFile(const string &filename)
{
  ifstream file(filename);
  vector<int> numbersVec;

  if (!file.is_open())
  {
    cerr << "Unable to open file: " << filename << endl;
    return numbersVec;
  }

  string line;

  if (getline(file, line))
  {
    stringstream lineAsStream(line);
    int num;
    while (lineAsStream >> num)
    { 
      numbersVec.push_back(num);
    }
  }

  file.close();
  return numbersVec;
}

bool binarySearch(const vector<int> &data, int number)
{
  int low = 0;
  int high = data.size() - 1;

  
    while (low<= high)
    {
      int mid = low + (high - low) /2;

      if (number == data[mid])
      {
        return true;
      }

      if (data[mid] < number)
      {
        low = mid + 1;
      }      

      else 
      {
        high = mid -1;
      }

    }

    return false;
      
}


int main(int argc, char const *argv[])
{
  if (argc < 3)
  {
    cerr << "Not enough arguments\n";
    return 1;
  }
  string data_file = argv[1];
  string numbers_file = argv[2];
  auto data = readIntsFromFile(data_file);
  auto numbers = readIntsFromFile(numbers_file);

  sort(data.begin(), data.end());


  for (long unsigned int i = 0; i < numbers.size(); i++)
  {
    if (binarySearch(data, numbers[i]) == true)
    {
      cout <<  numbers[i] << ": T" << endl; 
    }
    else
    {
      cout << numbers[i] << ": F"<<endl; 
    }
    
  }
  
  

  // for (int i = 0; i < data.size(); i++)
  // {
  //   cout << data[i] << endl;
  // }

  // cout << endl;

  // for (int i = 0; i < numbers.size(); i++)
  // {
  //   cout << numbers[i] << endl;
  // }
  return 0;
}
