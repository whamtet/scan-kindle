(ns scan-kindle.core)

(use 'seesaw.core)
(require '[clojure.java.io :as io])
(import javax.sound.sampled.AudioSystem)
(import javax.sound.sampled.AudioInputStream)
(import javax.imageio.ImageIO)
(import java.awt.Rectangle)
(import java.awt.Toolkit)

(defonce robot (java.awt.Robot.))

(defn play-sound []
  (doto
    (AudioSystem/getClip)
    (.open (AudioSystem/getAudioInputStream (io/input-stream "chimes.wav")))
    .start))

(def num-pics (atom 0))
(def screen-rect (Rectangle. (.getScreenSize (Toolkit/getDefaultToolkit))))

(defn save-screen []
  (ImageIO/write
    (.createScreenCapture robot screen-rect)
    "png"
    (java.io.File. (format "resources/%s.png" (swap! num-pics inc)))))

(defn focus-gained [e])

(defn focus-lost [e]
  (Thread/sleep 200)
  (save-screen)
  (play-sound))


(defn -main [& args]
  (invoke-later
    (let [f (frame :content "hello"
                   :on-close :exit)]
      (listen f :focus-lost #'focus-lost :focus-gained #'focus-gained)
      (-> f pack! show!))))

(defonce f (-main))
