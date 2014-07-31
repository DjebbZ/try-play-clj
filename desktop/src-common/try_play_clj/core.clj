(ns try-play-clj.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]))

(defn move
  [entity direction]
  (case direction
    :down (assoc entity :y (dec (:y entity)))
    :up (assoc entity :y (inc (:y entity)))
    :left (assoc entity :x (dec (:x entity)))
    :right (assoc entity :x (inc (:x entity)))
    nil))

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage) :camera (orthographic))
    (assoc (texture "lion.png")
           :x 50 :y 50 :width 100 :height 100
           :angle 45 :origin-x 50 :origin-y 50))

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))

  :on-key-down
  (fn [screen entities]
    (cond
      (= (:key screen) (key-code :dpad-up))
      (move (first entities) :up)
      (= (:key screen) (key-code :dpad-left))
      (move (first entities) :left)
      (= (:key screen) (key-code :dpad-right))
      (move (first entities) :right)
      (= (:key screen) (key-code :dpad-down))
      (move (first entities) :down)))

  :on-touch-down
  (fn [screen entities]
    (let [pos (input->screen screen (:input-x screen) (:input-y screen))]
      (cond
        (> (:y pos) (* (height screen) (/ 2 3)))
        (move (first entities) :up)
        (< (:y pos) (/ (height screen) 3))
        (move (first entities) :down)
        (> (:x pos) (* (width screen) (/ 2 3)))
        (move (first entities) :right)
        (< (:x pos) (/ (width screen) 3))
        (move (first entities) :left))))

  :on-resize
  (fn [screen entities]
    (height! screen 600)))

(defgame try-play-clj
  :on-create
  (fn [this]
    (set-screen! this main-screen)))
