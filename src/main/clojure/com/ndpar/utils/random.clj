(ns com.ndpar.utils.random
  (:import (com.ndpar.utils.random RandomUtils)))

(defn draw
  "Demo: calling Java from Clojure"
  [n N]
  (RandomUtils/draw n N))