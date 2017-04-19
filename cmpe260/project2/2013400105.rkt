#lang scheme
; compiling: yes
; complete: yes
; 2013400105

; (card-color card) -> string?
; card : pair?
;
; Returns the color of card
;
;Examples:
; > ( card-color '(D . A) )
; => 'red
; > ( card-color '(C . 10) )
; => 'black
(define (card-color card)
  (case (car card)
    ((H D) 'red)
    ((S C) 'black)
  )
)

; (card-rank card) -> number?
; card : pair?
;
; Return the rank of card
;
;Examples:
; > ( card-rank '(D . 3) )
; => 3
; > ( card-rank '(C . K) )
; => 10
(define (card-rank card)
  (case (cdr card)
    ((A) 11)
    ((K Q J) 10)
    (else (cdr card))
  )
)

; (all-same-color list-of-cards) -> boolean?
; list-of-cards : list?
;
; Returns #t if all the cards in list-of-cards have same color, else returns #f.
;
;Examples:
; > (all-same-color '((H . 3) (H . 2) (H . A) (D . A) (D . Q) (D . J)))
; => #t
; > (all-same-color '((H . 3) (H . 2) (H . A) (D . A) (D . Q) (C . J)))
; => #f
(define (all-same-color list-of-cards)
  (if (null? list-of-cards) #t
      (let ((first (card-color(car list-of-cards))))
        (null? (filter
                (lambda(x)(not (eqv? x first)))
                (map card-color (cdr list-of-cards)))
               )  
        )
   )  
)

; (fdraw list-of-cards held-cards) -> list?
; list-of-cards : list?
;
; Returns a new list of held-cards after the first card of the list-of-cards to held-cards.
; If list-of-cards is empty, it returns a empty list
;
;Examples:
; > ( fdraw '((H . 3) (H . 2) (H . A) (D . A) (D . Q) (D . J)) '())
; => '((H . 3))
; > ( fdraw '((H . 3) (H . 2) (H . A) (D . A) (D . Q) (D . J)) '((S . 3) (S . 2) (S . A)))
; => '((S . 3) (S . 2) (S . A) (H . 3))
; > ( fdraw '() '((S . 3) (S . 2) (S . A)))
; => '()
(define (fdraw list-of-cards held-cards)
  (cond
    ((null? list-of-cards) '())
    (else
     (let((draw (list (car list-of-cards))))
       (drop list-of-cards 1)
       (append held-cards draw)
      )
    )
  )
)

; (fdiscard list-of-cards list-of-moves goal held-cards) -> list?
; list-of-cards : list?
; list-of-moves : list?
; goal : number?
; held-cards : list?
; 
; Returns a new list of held-cards after the first card of held-cards removed.
; If held-cards is empty, it returns a empty list
; I don't any strategy
;
;Examples:
; > ( fdiscard '((C . 3) (C . 2) (C . A) (S . J) (S . Q) (H . J))
; '(draw draw draw discard)
; 66
; '((H . 3) (H . 2) (H . A) (D . A) (D . Q) (D . J)) )
; => '((H . 2) (H . A) (D . A) (D . Q) (D . J))
; > ( fdiscard '((H . 3) (H . 2) (H . A) (D . A) (D . Q) (D . J))
; '(draw draw draw discard)
; 56
; '((S . 3) (S . 2) (S . A) (C . A) (C . Q) (C . J)) )
; => '((S . 2) (S . A) (C . A) (C . Q) (C . J))
(define (fdiscard list-of-cards list-of-moves goal held-cards)
  (cond
    ((null? held-cards) '())
    (else (drop held-cards 1))
  )
)

; (find-steps list-of-cards list-of-moves goal) -> list?
; list-of-cards : list?
; list-of-moves : list?
; goal : number?
; 
; Returns a list of steps that is a list of pairs of moves and corresponding cards along the game.
; It use recursive 'find-nexts' function
;
;Examples:
; > ( find-steps '((H . 3) (H . 2) (H . A) (D . J) (D . Q) (C . J)) '(draw draw draw discard) 16 )
; => '((draw (H . 3)) (draw (H . 2)) (draw (H . A)) (discard (H . 3)))

(define (find-steps list-of-cards list-of-moves goal)
  (find-nexts list-of-cards list-of-moves '() goal '())
)

; (find-nexts list-of-cards list-of-moves held-cards goal list-of-steps) -> list?
; list-of-cards : list?
; list-of-moves : list?
; held-cards : list?
; goal : number?
; list-of-steps : list?
;
; Returns a list of steps recursively.
; held-cards and list-of-steps are empty inially
;
(define (find-nexts list-of-cards list-of-moves held-cards goal list-of-steps)
  (if (is-game-over list-of-cards list-of-moves held-cards goal)
      list-of-steps ; Game is over!!
      (cond
        ((eqv? (car list-of-moves) 'draw)
         (find-nexts
          (cdr list-of-cards)
          (cdr list-of-moves)
          (fdraw list-of-cards held-cards)
          goal
          (append list-of-steps (list (cons 'draw (list (car list-of-cards)))))
         )
        )
        ((eqv? (car list-of-moves) 'discard)
         (find-nexts
          list-of-cards
          (cdr list-of-moves)
          (fdiscard list-of-cards list-of-moves goal held-cards)
           goal
           (append list-of-steps (list (cons 'discard (list (car held-cards)))))
          )
       )
     ) 
  )
)
; (find-held-cards list-of-steps) -> list?
; list-of-steps : list?
;
; Returns the list of held-cards after the list-of-steps is applied.
; If it's draw append the card to helds, if it's discard remove the card from helds.
;
;Examples:
; > ( find-held-cards '((draw (H . 3)) (draw (H . 2)) (draw (H . A)) (discard (H . 3))) )
; => '((H . 2) (H . A))
(define (find-held-cards list-of-steps)
  (foldl (lambda(pair helds)(cond
                              ((eqv? (car pair) 'draw) (append helds (cdr pair)))
                              ((eqv? (car pair) 'discard) (remove (cadr pair) helds))
                             )
           ) '() list-of-steps)
)

; (calc-playerpoint list-of-cards) -> number?
; list-of-cards : list?
;
; Calculates and returns the corresponding playerpoint for list-of-cards.
; Adds up rank of the cards that in the list-of-cards
;
;Examples:
; > ( calc-playerpoint '((H . A) (H . 3) (H . 2) (D . Q) (D . J) (C . J)) )
; => 46
(define (calc-playerpoint list-of-cards)
 (foldl + 0 (map card-rank list-of-cards))
)

; (calc-score list-of-cards goal) -> number?
; list-of-cards : list?
; goal : number?
;
; Calculates and returns finalscore based on prescore, playerpoint and list-of-cards.
;
;Examples:
; > ( calc-score '((H . 3) (H . 2) (H . A) (D . J) (D . Q) (C . J)) 50 )
; => 4
; > ( calc-score '((H . 3) (H . 2) (H . A) (D . J) (D . Q) (C . J)) 16 )
; => 150
(define (calc-score list-of-cards goal)
  (let* ((playerpoint (calc-playerpoint list-of-cards))
        (prescore (cond
                    ((> playerpoint goal) (* 5 (- playerpoint goal)))
                    (else (- goal playerpoint))
                  )
         )
    )
    (cond
      ((all-same-color list-of-cards) (floor (/ prescore 2)))
      (else prescore)
    )
  )
)

; (is-game-over list-of-cards list-of-moves held-cards goal) -> boolean?
; list-of-cards : list?
; list-of-moves : list?
; held-cards : list?
; goal : number?
;
; Checks is the game over based on 4 rules.
; Rule 1: If the list of moves is empty, the game ends.
; Rule 2: If the current move is draw and the card-list is empty, the game ends.
; Rule 3: If the current move is discard and the held-cards is empty, the game ends.
; Rule 4: At any time if the sum of the values in the held-cards is greater than the goal after the last move,
;
;Examples:
; > (is-game-over '((H . 2)) '() '() 22) ; Rule 1
; => #t
; > (is-game-over '() '(draw) '() 35) ; Rule 2
; => #t
; > (is-game-over '((H . 5)) '(discard) '() 35) ; Rule 3
; => #t
(define (is-game-over list-of-cards list-of-moves held-cards goal)
  (or
   (null? list-of-moves) ; Rule 1
   (and (equal? (car list-of-moves) 'draw) (null? list-of-cards)) ; Rule 2
   (and (equal? (car list-of-moves) 'discard) (null? held-cards)) ; Rule 3
   (> (calc-playerpoint held-cards) goal) ; Rule 4
  )
)

; (play list-of-cards list-of-moves goal) -> number?
; list-of-cards : list?
; list-of-moves : list?
; goal : number?
;
; Plays the game with defined methods
;Examples:
; > ( play '((H . 3) (H . 2) (H . A) (D . J) (D . Q) (C . J)) '(draw draw draw discard) 16 )
; => 1
(define (play list-of-cards list-of-moves goal)
  (calc-score (find-held-cards (find-steps list-of-cards list-of-moves goal)) goal))