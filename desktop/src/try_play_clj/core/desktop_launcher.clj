(ns try-play-clj.core.desktop-launcher
  (:require [try-play-clj.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main
  []
  (LwjglApplication. try-play-clj "try-play-clj" 800 600)
  (Keyboard/enableRepeatEvents true))
