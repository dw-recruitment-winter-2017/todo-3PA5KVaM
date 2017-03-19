(ns dw-todo.datastore)

;; Storing todo items in this atom for now.
;; In a real app, this would be backed up by a database,
;; rather than just storing in memory.

;; Status states:
;; 0 = incomplete
;; 1 = complete
;; 2 = inactive

(defonce data (atom { :todos [
  {:id 0 :status 0 :text "Learn Clojure"}
  {:id 1 :status 0 :text "Build a Todo App"}]}))

(defn get-todos []
  (get-in @data [:todos]))

;; Only get the active todos (ie not "deleted")
(defn get-active-todos []
  (vec (filter #(< (:status %) 2) (get-todos))))

(defn add-todo [todo]
  (let [new-max  (count (get-todos))
        new-item (assoc-in todo [:id] new-max)]
    (swap! data update-in [:todos] merge new-item)
    (identity new-item)))

(defn add-todos [todos]
  (mapv add-todo todos))

(defn update-todo [todo]
  (do
    (swap! data update-in [:todos] assoc (:id todo) todo)
    (get-in @data [:todos (:id todo)])))
