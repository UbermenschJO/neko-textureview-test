(ns test.views.main
  (:use [neko.activity :only [defactivity set-content-view!]]
        [neko.threading :only [on-ui]]
        [neko.ui :only [make-ui]]
        [neko.ui.mapping :only [defelement]]
        [neko.resource :only [get-resource]]
        [neko.notify :only [toast]]
        [test.views.textureview]
        [test.views.camera]))

(defn my-view [a]
  "return my view tree"
  (prepare-camera a)
  (vector :texture-view
          {:surface-texture-listener
           (surface-texture-listener
            :on-surface-texture-available (fn [surface width height]
                                            (.setPreviewTexture ^android.hardware.Camera (get-current-camera) surface)
                                            (.startPreview ^android.hardware.Camera (get-current-camera)))
            :on-surface-texture-destroyed (fn [surface]
                                            (.stopPreview ^android.hardware.Camera (get-current-camera))
                                            (.release ^android.hardware.Camera (get-current-camera))))}))

(defactivity test.views.MainActivity
  :def a
  :on-create
  (fn [this bundle]
    (on-ui
     (set-content-view! a
                        (make-ui [:linear-layout {:orientation :vertical}
                                  (my-view a)
                                  [:button {:text "Connect" :on-click (fn [w] (toast "Connected!") )}]
                                  ])))))
