#include <boost/mpi.hpp>
#include <iostream>
#include <vector>
#include <fstream>

/**
 * It's rank of the master processor
 */
#define MASTER_PROCESSOR 0

using namespace std;
namespace mpi = boost::mpi;

/**
 * Word struct keeps information of string and it's count
 */
struct Word {
public:
    string text;
    int count;

    /**
     * Default constructor
     */
    Word() {
        text = "";
        count = 0;
    }

    /**
     * Default constructor
     */
    Word(string text_, int count_) {
        text = text_;
        count = count_;
    }

    /**
     * Override equal operator
     * @param w Word to compare with this one
     * @return true if text of the this word is equal to other one
     */
    bool operator==(Word w) const {
        return text == w.text;
    }


private:
    friend class boost::serialization::access;

    /**
     * Serializes Word struct for sending via Boost MPI
     */
    template<class Archive>
    void serialize(Archive &ar, const unsigned int version) {
        ar & text;
        ar & count;
    }

};

/**
 * Compare two words depends on their text
 */
struct wordComparator {
    bool operator()(Word w1, Word w2) {
        return w1.text < w2.text;
    }
};

/**
 * Entry point of my application
 */
int main(int argc, char *argv[]) {
    mpi::environment env(argc, argv);
    mpi::communicator world;

    /**
     * Master processor work
     */
    if (world.rank() == MASTER_PROCESSOR) {
        // Contains raw strings
        vector<string> strings;

        // Checks arguments count for input and output file
        if (argc < 2) {
            // If no input file name is give, raise an error
            cout << "Run the code with the following command: mpirun --oversubscribe -np [processor_count] main.o [input_file] [output_file]?" << endl;
            return 1;
        } else {
            // Read raw strings
            ifstream in(argv[1]);
            string line;
            while (in >> line) {
                strings.push_back(line);
            }
            in.close();
            // If output file is given, print out to it
            if (argc == 3) {
                freopen(argv[2], "w", stdout);
            }
        }

        // Divide raw string array and send to slaves
        for (int i = 1; i < world.size(); i++) {
            vector<string> substrings;
            for (int j = 0; j < strings.size(); j++) {
                if (j % (world.size() - 1) == i - 1) substrings.push_back(strings[j]);
            }
            world.send(i, 1, substrings);
        }

        // Collect mapped word struct vectors from slaves
        vector<Word> words;
        for (int i = 1; i < world.size(); ++i) {
            vector<Word> subwords;
            world.recv(i, 2, subwords);
            words.insert(words.end(), subwords.begin(), subwords.end());
        }

        // Divide mapped word vector and send to slaves
        for (int i = 1; i < world.size(); i++) {
            vector<Word> subwords;
            for (int j = 0; j < words.size(); j++) {
                if (j % (world.size() - 1) == i - 1) subwords.push_back(words[j]);
            }
            world.send(i, 3, subwords);
        }

        // Collect sorted mapped word struct vectors from slaves and merge
        vector<Word> sorted_words;
        for (int i = 1; i < world.size(); ++i) {
            vector<Word> subwords;
            world.recv(i, 4, subwords);
            vector<Word> temp_words(sorted_words.size() + subwords.size());
            // Merge sorted vectors that come from slaved
            merge(sorted_words.begin(),  sorted_words.end(), subwords.begin(), subwords.end(), temp_words.begin(), wordComparator());
            swap(sorted_words, temp_words);
        }

        // Reduce words vector and sum up their counts
        vector<Word> reduced_words;
        Word last_word = sorted_words.front();
        for (int i = 1; i < sorted_words.size() + 1; i++) {
            if (last_word == sorted_words[i]) {
                // If the last one is same word, increase count
                last_word.count++;
            } else {
                // If it's different word, push it to reduced one and change last word
                reduced_words.push_back(last_word);
                last_word = sorted_words[i];
            }
        }

        // Print out strings and their counts
        for (int i = 0; i < reduced_words.size(); i++) {
            cout << reduced_words[i].text << " " << reduced_words[i].count << endl;
        }
    } else {
        /**
         * Slave processors works
         */

        // Receive raw string vector
        vector<string> strings;
        world.recv(MASTER_PROCESSOR, 1, strings);

        // Map string to Word with count
        vector<Word> words;
        for (int i = 0; i < strings.size(); i++) {
            words.push_back(Word(strings[i], 1));
        }
        // Sent back to mapped struct to master
        world.send(MASTER_PROCESSOR, 2, words);

        // Receive mapped vectors to sort
        vector<Word> words_to_sort;
        world.recv(MASTER_PROCESSOR, 3, words_to_sort);

        // Sort subvectors
        sort(words_to_sort.begin(), words_to_sort.end(), wordComparator());

        // Send sorted vectors to master
        world.send(MASTER_PROCESSOR, 4, words_to_sort);
    }
    return 0;
}
