(ns dw-todo.datastore)

(defonce data (atom { :todos [
  {:id 0 :done (boolean false) :text "Learn Clojure"}
  {:id 1 :done (boolean false) :text "Build a Todo App"}]}))

(defn get-todos []
  (get-in @data [:todos]))

(defn add-todo [todo]
  (let [new-max  (count (get-todos))
        new-item (assoc-in todo [:id] new-max)]
    (swap! data update-in [:todos] merge new-item)
    (identity new-item)))

(defn add-todos [todos]
  (mapv add-todo todos))
