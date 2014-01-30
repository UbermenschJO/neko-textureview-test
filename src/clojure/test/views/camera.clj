(ns test.views.camera
  "android Camera 관련"
  {:author "Jo Jung Lae"}
  (:use [neko.debug :only [safe-for-ui]])
  (:import android.view.Surface
           [android.hardware Camera Camera$CameraInfo]))

(def degrees (atom 0))
(def cam (atom {:camera nil :camera-info nil}))

(defn get-current-camera []
  (:camera @cam))

(defn get-current-camera-info []
  (:camera-info @cam))

(defn set-orientation [a]
  (let [r (-> a .getWindowManager
              (.getDefaultDisplay)
              (.getRotation))]
    (cond
     (= r (android.view.Surface/ROTATION_0)) (swap! degrees + 0)
     (= r (android.view.Surface/ROTATION_90)) (swap! degrees + 90)
     (= r (android.view.Surface/ROTATION_180)) (swap! degrees + 180)
     (= r (android.view.Surface/ROTATION_270)) (swap! degrees + 270))
    (.setDisplayOrientation ^android.hardware.Camera (get-current-camera)
                            (mod (- 360 (mod (+ (. ^android.hardware.Camera$CameraInfo (get-current-camera-info) orientation) @degrees) 360)) 360))))

(defn prepare-camera
  "prepare Front Camera"
  [activity]
  (safe-for-ui
   (let [info (android.hardware.Camera$CameraInfo.)
         c (android.hardware.Camera/open 1)]
     (android.hardware.Camera/getCameraInfo 1 info)
     (swap! cam update-in [:camera] (fn [_] c))
     (swap! cam update-in [:camera-info] (fn [_] info))
     (set-orientation activity))))
