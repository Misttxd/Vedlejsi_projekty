#include <iostream>
#include <vector>
#include <algorithm>
#include <fstream>
#include <sstream>

using std::vector;

//upraveny counting sort
void countingSortByDigit(vector<int>& data, int exp){
    if(data.size() < 2){
        return;
    }
    
    vector<int> sortedOutput(data.size());
    vector<int> count(10, 0); 
    
    // Spocita vyskyty cislic
    for(long unsigned int i = 0; i < data.size(); i++){
        int digit = (data[i] / exp) % 10;
        count[digit]++;
    }
    
    // Vypocita kumulativni pozice
    for(int i = 1; i < 10; i++){
        count[i] += count[i - 1];
    }
    
    // sestavi serazene pole
    for(int i = data.size() - 1; i >= 0; i--){
        int digit = (data[i] / exp) % 10;
        sortedOutput[count[digit] - 1] = data[i];
        count[digit]--;
    }
    
    data = sortedOutput;
}

void radixSort(vector<int>& data){
    if(data.size() < 2){
        return;
    }
    
    // Najde maximum 
    int max = data[0];
    for(long unsigned int i = 1; i < data.size(); i++){
        if(data[i] > max){
            max = data[i];
        }
    }
    
    for(int exp = 1; max / exp > 0; exp *= 10){
        countingSortByDigit(data, exp);
    }
}


//ukradeny system pro cteni ze souboru :)
std::vector<int> readIntegersFromFile(const std::string& filename) {
    std::ifstream file(filename);
    std::vector<int> numbersVec;

    if (!file.is_open()) {
        std::cerr << "Unable to open file: " << filename << std::endl;
        return numbersVec;
    }

    std::string line;

    // change if to while to read the whole file
    if (std::getline(file, line)) {
        std::stringstream lineAsStream(line);
        int num;
        while (lineAsStream >> num) {  // NOTE: this assumes that the input file has the data we want
            numbersVec.push_back(num);
        }
    }

    file.close();
    return numbersVec;
}

//taky ukradene (nechtelo se mi to moc predelavat :)
void test2(std::string filename){
    std::vector<int> numbers;

    numbers = readIntegersFromFile(filename);

    /*for(const auto& item : numbers){
        std::cout << item << " ";
    }
    std::cout << "\n";*/

    auto data2 = numbers;
    radixSort(numbers);
    std::sort(data2.begin(), data2.end());

    for(size_t i = 0; i < numbers.size(); i++){
        std::cout << numbers[i] << " ";
        /*if  (i < numbers.size()){
            std::cout << " " ;
        }/*/
      
    }
    std::cout << std::endl;
    return;
}

int main([[maybe_unused]] int argc, char* argv[]) {
    std::string filename = argv[1];
    test2(filename);
    return 0;
}
