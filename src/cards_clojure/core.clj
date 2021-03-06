(ns cards-clojure.core)

(def suits [:clubs :spades :hearts :diamonds])
(def ranks (range 1 14))

;"helper" functions

(defn ascend-count [hand]
  (loop [i 5 result [] num (first(sort(map :rank hand)))]
    (if (zero? i)
      (sort result)
      (recur (dec i)
       (conj result
         num) (inc num) ))))

(defn drop-first-n [n hand]
  (drop n (sort (map :rank hand))))

(defn drop-last-n [n hand]
  (drop-last n (sort (map :rank hand))))

;creation functions + test hand
(defn create-deck []
  (set
    ;loops over all ranks for each suit
    (for [
          suit suits
          rank ranks]
      {:suit suit :rank rank})))

(def test-hand #{{:suit :diamonds, :rank 3}
  {:suit :clubs, :rank 3}
  {:suit :hearts, :rank 5}
  {:suit :hearts, :rank 3}
 {:suit :spades, :rank 3}})

(defn create-hands [deck]
  (set
    (for [card1 deck
          ;disj returns deck without card1
          card2 (disj deck card1)
          card3 (disj deck card1 card2)
          card4 (disj deck card1 card2 card3)
          card5 (disj deck card1 card2 card3 card4)]
      #{card1 card2 card3 card4 card5})))

(defn winning-hand [hand1 hand2]
  ;hand rankings
  (let [royal-flush 1
        straight-flush 2
        four-of-a-kind 3
        full-house 4
        flush 5
        straight 6
        three-of-a-kind 7
        two-pair 8
        pair 9]
    ))

;poker hands

(defn four-of-a-kind? [hand]
  (or
    (apply = (drop-first-n 1 hand))
    (apply = (drop-last-n 1 hand))))

(defn full-house? [hand]
  (or
    (and
      (apply = (drop-first-n 3 hand))
      (apply = (drop-last-n 2 hand)))
    (and
      (apply = (drop-first-n 2 hand))
      (apply = (drop-last-n 3 hand)))))

(defn flush? [hand]
  (= 1 (count (set (map :suit hand)))))

(defn high-straight? [hand]
  (= (sort (map :rank hand)) '(1 10 11 12 13)))

(defn straight? [hand]
  (or (= (sort (map :rank hand))
         (ascend-count hand))
      (high-straight? hand)))

(defn straight-flush? [hand]
  (and (straight? hand) (flush? hand)))

(defn royal-flush? [hand]
  (and (straight-flush? hand)
       (=
         (sort (map :rank hand))
         ('(1 10 11 12 13)))))

(defn three-of-a-kind? [hand]
  (or
    (apply = (drop-first-n 2 hand))
    (apply = (drop-last-n 2 hand))
    (apply = (drop-first-n 1 (drop 1 hand))))
  )

;todo fix end/begin cases
(defn two-pair? [hand]
  (or
    ;middle case
    (and
      (apply = (drop-first-n 3 hand))
      (apply = (drop-last-n 3 hand)))

    ;end case
    (and
      (apply = (drop 2(drop-first-n 1 hand)))
      (apply = (drop-last 2(drop-first-n 1 hand))))

    ;beginning case
    (and
      (apply = (drop 2(drop-last-n 1 hand)))
      (apply = (drop-last 2(drop-last-n 1 hand))))))

(defn pair? [hand]
  (not= (count (dedupe (map :rank hand))) 5))

(defn -main []
  (let [deck (create-deck)
        hands (create-hands deck)
        flush-hands (filter flush? hands)]
    (println (count flush-hands))))