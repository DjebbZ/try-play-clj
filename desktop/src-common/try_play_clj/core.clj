(ns try-play-clj.core
  (:import (com.badlogic.gdx.scenes.scene2d.ui TextButton$TextButtonStyle)
           (com.badlogic.gdx.scenes.scene2d EventListener)
           (com.badlogic.gdx.scenes.scene2d.utils ClickListener))
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.ui :refer :all]))

(declare try-play-clj main-screen text-screen button-screen)

(defn move
  [entity direction]
  (case direction
    :down (assoc entity :y (dec (:y entity)))
    :up (assoc entity :y (inc (:y entity)))
    :left (assoc entity :x (dec (:x entity)))
    :right (assoc entity :x (inc (:x entity)))
    nil))

(defn move-first
  [entities direction]
  (vec (conj (rest entities) (move (first entities) direction))))

(defn create-lion [max-width max-height]
  (assoc (texture "lion.png")
         :x (rand-int max-width)
         :y (rand-int max-height)
         :width 100 :height 100
         :angle (rand-int 360)
         :origin-x 50 :origin-y 50))

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage) :camera (orthographic))
    (create-lion (width screen) (height screen)))

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))

  :on-key-down
  (fn [screen entities]
    (cond
      (= (:key screen) (key-code :dpad-up))
      (move-first entities :up)
      (= (:key screen) (key-code :dpad-left))
      (move-first entities :left)
      (= (:key screen) (key-code :dpad-right))
      (move-first entities :right)
      (= (:key screen) (key-code :dpad-down))
      (move-first entities :down)))

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
    (height! screen 600))

  :on-clicked-spawn
  (fn [screen entities]
    (conj entities (create-lion (width screen) (height screen)))))

(defscreen text-screen
   :on-show
   (fn [screen entities]
     (update! screen :camera (orthographic) :renderer (stage))
     (assoc (label "0" (color :white))
            :id :fps
            :x 5))

   :on-render
   (fn [screen entities]
     (->> (for [entity entities]
            (case (:id entity)
              :fps (doto entity (label! :set-text (str (game :fps))))
              entity))
          (render! screen)))

   :on-resize
   (fn [screen entites]
     (height! screen 300)))

(defscreen button-screen
   :on-show
   (fn [screen entities]
     (update! screen :camera (orthographic) :renderer (stage))
     (assoc (label "Spawn" (color :white))
            :x 200 :y 200))

   :on-render
   (fn [screen entities]
     (render! screen entities))

   :on-ui-touch-down
   (fn [screen entities]
     (println (:event screen))
     (println (:input-x screen))
     (println (:input-y screen))
     (println (:pointer screen))
     (println (:button screen))
     (run! main-screen :on-clicked-spawn)
     entities)

   :on-resize
   (fn [screen entites]
     (height! screen 300)))

(defgame try-play-clj
  :on-create
  (fn [this]
    (set-screen! this main-screen button-screen text-screen)))
