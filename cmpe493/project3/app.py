import os, sys
import math
import numpy as np
from rouge import Rouge

# CONSTANTS
COSINE_SIMILARITY_THRESHOLD = 0.10
TELEPORTATION_RATE = 0.15
ERROR_TOLERANCE = 0.00001

def read_news(directory):
    """
        Reads news from files. Returns news and summaries
    """

    filenames = os.listdir(directory)
    # Get all file with .txt extension
    filenames = [filename for filename in filenames if filename.endswith(".txt")]
    filenames.sort()
    news = {}
    summaries = {}
    # Extract news from each file
    for filename in filenames:
        raw_data = open(os.path.join(directory, filename), "r", encoding="utf-8").read()
        raw_news = raw_data.split('\n\n')
        news[filename] = raw_news[0].strip().split('\n')
        summaries[filename] = raw_news[1].strip()
    return news, summaries

def calculate_idf(news):
    """
        Calculates idf for given news 
    """

    df = {}
    idf = {}
    N = len(news)
    # Count document frequency for each term
    for text in news.values():
        tokens = " ".join(text).split()
        terms = set(tokens)
        for term in terms:
            df[term] = df.get(term, 0) + 1
    # Calculate idf for each term
    for term, freq in df.items():
        idf[term] = math.log10(N / freq)
    return idf

def calculate_tf_idf(sentence, idf, terms):
    """
        Calculates tf idf vector for given sentence 
    """

    counts = {}
    # Count term frequencies
    for token in sentence.split():
        counts[token] = counts.get(token, 0) + 1
    
    tf_idf = []
    for term in terms:
        # Calculate tf
        tf = 1 + math.log10(counts.get(term, 0.1))
        # Add tf idf to result
        tf_idf.append(tf * idf.get(term))
    return tf_idf

def unit_vector(vec):
    """
        Returns unit vector that points same direction with given vector 
    """

    return vec / np.linalg.norm(vec)

def cosine_similarity(vec1, vec2):
    """
        Calculated cosine similarity between given two vectors 
    """

    return np.dot(unit_vector(vec1), unit_vector(vec2))

def print_matrix(m):
    """
        Prints matrix prettier
    """
    for row in m:
        text = ""
        for column in row:
            text += "{:^10.5f}".format(column)
        print(text)

def power_iteration(m):
    """
        Returns eagen vector of given matrix 
    """
    x = [1/len(m)] * len(m)
    while True:
        x_new = np.matmul(x, m)
        for i in range(len(m)):
            if abs(x_new[i] - x[i]) >= ERROR_TOLERANCE:
                break
            return x_new
        x = x_new

def calculate_lex_rank(sentences, idf):
    """
        Calculates lex rank of given sentences 
    """

    lex_ranks = []
    tf_idf = []
    terms = list(set(" ".join(sentences).split()))
    dim = len(sentences)
    # Calculate tf idfs of sentences
    for sentence in sentences:
        tf_idf.append(calculate_tf_idf(sentence, idf, terms))

    # Build adjencency matrix with 0 and 1
    adj_mat = []
    for x in range(dim):
        adj_mat.append([])
        for y in range(dim):
            cos_sim = cosine_similarity(tf_idf[x], tf_idf[y])
            adj_mat[x].append(1 if (cos_sim >= COSINE_SIMILARITY_THRESHOLD) else 0)

    # Convert adjencency matrix to probability matrix with teleportation rate
    for x in range(dim):
        N = sum(adj_mat[x])
        for y in range(dim):
            adj_mat[x][y] = ((adj_mat[x][y] / N) * (1 - TELEPORTATION_RATE)) + (TELEPORTATION_RATE / dim)
    
    return list(power_iteration(adj_mat))

def summarize(sentences, idf):
    """
        Generates summary for given sentences 
    """

    maxest = []
    lex_rank = calculate_lex_rank(sentences, idf)

    # Find three sentences indexes that have highest lex rank
    for i in range(3):
        max_lex = max(lex_rank)
        max_index = lex_rank.index(max_lex)
        maxest.append(max_index)
        lex_rank[max_index] = 0

    # Sort indexes. Don't change topic flow
    maxest.sort()
    summary = []
    for index in maxest:
        summary.append(sentences[index])

    return "\n".join(summary)

###############################
####### APP ENTRY POINT #######
###############################

# Check arguments
if len(sys.argv) < 2:
    print("You have to give command name")
    print("python3 app.py [COMMAND] [DATA_DIRECTORY] [FILE_NAME]")
    exit(1)
elif len(sys.argv) < 3:
    print("You have to give directory name")
    print("python3 app.py [COMMAND] [DATA_DIRECTORY] [FILE_NAME]")
    exit(1)

command = sys.argv[1]
directory = sys.argv[2]

# Read data set
news, summaries = read_news(directory)

# if no file is given process all of them
files = [sys.argv[3]] if len(sys.argv) == 4 else news.keys()

# Calculate IDFs
idf = calculate_idf(news)

# Run command
if command == "lex":
    for file in files:
        lex_rank = calculate_lex_rank(news[file], idf)
        print(" ".join(["{:.3f}".format(rank) for rank in lex_rank]))
elif command == "summary":
    for file in files:
        print(summarize(news[file], idf))
elif command == "gold":
    for file in files:
        print(summaries[file])
elif command == "rouge":
    rouge = Rouge()
    total = {}
    total["rouge-1"] = {"f":0, "r":0, "p":0}
    total["rouge-2"] = {"f":0, "r":0, "p":0}
    total["rouge-l"] = {"f":0, "r":0, "p":0}
    for file in files:
        generated_summary = summarize(news[file], idf)
        gold_summary = summaries[file]
        scores = rouge.get_scores(gold_summary, generated_summary)
        for type in ["rouge-1", "rouge-2", "rouge-l"]:
            for stat in ["p", "r", "f"]:
                total[type][stat] += scores[0][type][stat]
    
    # Divide sum to lenghts
    for type in ["rouge-1", "rouge-2", "rouge-l"]:
        for stat in ["p", "r", "f"]:
            total[type][stat] /= len(files)
    
    print("Average Rouge Scores")
    for type in ["rouge-1", "rouge-2", "rouge-l"]:
        print(type)
        for stat in ["p", "r", "f"]:
            print("\t" + stat + ": " + str(total[type][stat]))
        print("\n")