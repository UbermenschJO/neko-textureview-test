(ns test.views.textureview
  "TextureView 대응 clojure file"
  {:author "Jo Jung Lae"}
  (:require neko.ui)
  (:use [neko.ui.mapping :only [defelement]]
        [neko.ui.traits :only [deftrait]]
        [neko.-utils :only [call-if-nnil]])
  (:import (android.view TextureView TextureView$SurfaceTextureListener)))

;;; implements SurfaceTextureListener

(defn surface-texture-listener
  "Create SurfaceTextureListener"
  [& {:keys [on-surface-texture-updated on-surface-texture-destroyed on-surface-texture-destroyed on-surface-texture-available on-surface-texture-size-changed]}]
  (reify android.view.TextureView$SurfaceTextureListener
    (onSurfaceTextureUpdated [this surface]
      (call-if-nnil on-surface-texture-updated surface))
    (onSurfaceTextureDestroyed [this surface]
      (call-if-nnil on-surface-texture-destroyed surface))
    (onSurfaceTextureAvailable [this surface width height]
      (call-if-nnil on-surface-texture-available surface width height))
    (onSurfaceTextureSizeChanged [this surface width height]
      (call-if-nnil on-surface-texture-size-changed surface width height))))

;;; neko bindings

(defelement :texture-view
  :classname android.view.TextureView
  :inherits nil
  :traits [:surface-texture-listener])

(deftrait :surface-texture-listener
  "Takes `:surface-texture-listener"
  [^TextureView tview, {:keys [surface-texture-listener]} _]
  (.setSurfaceTextureListener tview surface-texture-listener))
