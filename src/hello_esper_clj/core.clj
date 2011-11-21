(ns hello-esper-clj.core)
  (:use [clj-esper.core] :reload)
  (:import [com.espertech.esper.event.map MapEventBean]
           [com.espertech.esper.client EPServiceProviderManager]
           [com.espertech.esper.core EPServiceProviderImpl]))



(defevent TestEvent [a :int b :string])
(defevent OtherEvent [a :string])
(defstatement select-test "SELECT a, b FROM TestEvent")
(defstatement select-other "SELECT a FROM OtherEvent")

(def other-result (atom []))
(def result (atom []))

(defn handler
  [atom]
  (fn [x]
    (swap! atom conj x)))

(defn setup-service []
  (with-esper service {:events #{TestEvent OtherEvent}}
    (attach-statement select-test (handler result))
    (attach-statement select-other (handler other-result))
    (trigger-event (new-event TestEvent :a 1 :b "Hello"))
                   ))


(defn -main []
  (setup-service))


