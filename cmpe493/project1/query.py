import re
from enum import Enum
from tokenizer import Tokenizer

class Query:
    """
        Handles query operations
    """

    def __init__(self, query):
        """
            Constructs an new query with given one
        """
        self.type, self.query = Query.extract(query)

    @staticmethod
    def extract(query):
        """
            Returns query's text and it's type
            If no type is given, it guesses type of the query
            Returns: 
                type, query 
        """
        type = re.match(r'^\d+', query)
        if type:
            # Get type of the query from text and return with query
            return int(type.group()), re.sub(r'^\d+\s', '', query)
        else:
            # Guess type of the query
            # If it has AND it's conjunctive
            # If it has /NUMBER it's proximity
            # Otherwise, it's phrase
            if 'AND' in query:
                return QueryType.CONJUNCTIVE, query
            elif re.match(r'.*?/\d+.*?', query):
                return QueryType.PROXIMITY, query
            else:
                return QueryType.PHRASE, query

    def run(self, dictionary, index):
        """
            Runs query depends on it's type
            Phrase queries are same with proximity queries.
            Just replace spaces with /0
        """
        result = []
        if self.type == QueryType.CONJUNCTIVE:
            result = self.run_conjunctive(dictionary, index)
        elif self.type == QueryType.PHRASE:
            self.query = self.query.replace(' ', ' /0 ')
            result = self.run_proximity(dictionary, index)
        elif self.type == QueryType.PROXIMITY:
            result = self.run_proximity(dictionary, index)
        else:
            print("Unknown query type")
        return result

    def run_conjunctive(self, dictionary, index):
        """
            Runs conjunctive query
        """
        # Tokenize and normalize query
        tokens = self.query.split(' AND ')
        stems = [Tokenizer.normalize_and_stem(token) for token in tokens]
        # Get inverted indexies of all stems
        postings_list = []
        for stem in stems:
            posting_id = dictionary.get(stem)
            postings = index.get(posting_id, {})
            postings_list.append(postings)
        # Intersect given lists
        return self.intersect_list(postings_list)

    def run_proximity(self, dictionary, index):
        """
            Runs proximity query
        """
        result = []
        # Tokenize and normalize query
        tokens = re.split(r'\s\/\d+\s', self.query)
        stems = [Tokenizer.normalize_and_stem(token) for token in tokens]
        # Get proximities
        proximities = [int(proximity) for proximity in re.findall(r'\d+', self.query)]
        # Get inverted indexies of all stems
        postings_list = []
        for stem in stems:
            posting_id = dictionary.get(stem)
            postings = index.get(posting_id, {})
            postings_list.append(postings)
        # Intersect positionally given lists
        return self.positional_intersect_list(postings_list, proximities)

    def intersect(self, ps1, ps2):
        """
            Intersects two given lists
            Algorithm is based on Figure 1.6 from book
        """
        answer = []
        p1 = next(ps1)
        p2 = next(ps2)
        while True:
            try:
                if p1 == p2:
                    # If document's IDs are same add to answers
                    answer.append(p1)
                    p1 = next(ps1)
                    p2 = next(ps2)
                elif p1 < p2:
                    p1 = next(ps1)
                else:
                    p2 = next(ps2)
            except StopIteration:
                break
        return answer

    def intersect_list(self, postings_list):
        """
            Intersects multiple lists
            Algorithm is based on Figure 1.7 from book
        """
        # Sort them by their frequencies
        postings_list.sort(key=lambda postings: len(postings))

        # Intersect lists 2 by 2
        result = postings_list.pop(0)
        while result and postings_list:
            try:
                postings = postings_list.pop(0)
                result = self.intersect(iter(result), iter(postings))
            except IndexError:
                break
        if isinstance(result, dict):
            result = list(result.keys())
        return result

    def positional_intersect_list(self, postings_list, proximities):
        """
            Intersects positionally multiple lists
            Intersect next list with previous answer
        """
        start_postings = postings_list.pop(0)
        result = start_postings.keys()
        while postings_list and proximities:
            try:
                next_postings = postings_list.pop(0)
                k = proximities.pop(0)
                result, start_postings = self.positional_intersect(start_postings, next_postings, k)
                if not result:
                    break
            except IndexError:
                break
        return result

    def positional_intersect(self, ps1, ps2, k):
        """
            Intersects two given lists by proximity
            Algorithm is based on Figure 2.12 from book
            When you said next to last element,
            Python throws and exception instead of just return None
            So my method has too many try:except blocks
        """

        k = k + 1
        answer = set()
        postings = {}
        ps1_iter = iter(ps1)
        ps2_iter = iter(ps2)
        try:
            p1 = next(ps1_iter)
            p2 = next(ps2_iter)
        except StopIteration:
            return answer, postings
        while True:
            try:
                if p1 == p2:
                    l = []
                    p1_positions = iter(ps1[p1])
                    p2_positions = iter(ps2[p2])
                    pp1 = next(p1_positions)
                    pp2 = next(p2_positions)
                    while True:
                        try:
                            while True:
                                try:
                                    if pp2 - pp1 <= k and pp2 - pp1 > 0:
                                        l.append(pp2)
                                    elif pp2 > pp1:
                                        break
                                    pp2 = next(p2_positions)
                                except StopIteration:
                                    break
                            for ps in l:
                                answer.add(p1)
                                positions = postings.get(p1, [])
                                positions.append(ps)
                                postings[p1] = positions
                            pp1 = next(p1_positions)
                        except StopIteration:
                            break
                    p1 = next(ps1_iter)
                    p2 = next(ps2_iter)
                elif p1 < p2:
                    p1 = next(ps1_iter)
                else:
                    p2 = next(ps2_iter)
            except StopIteration:
                break
        return answer, postings

class QueryType:
    """
        Enumerates type of the query
    """
    CONJUNCTIVE = 1
    PHRASE      = 2
    PROXIMITY   = 3
