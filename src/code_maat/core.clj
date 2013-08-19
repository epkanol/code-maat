(ns code-maat.core
  (:require [code-maat.app.app :as app]
            [clojure.tools.cli :as cli]
            [clj-time.format :as time-format]))

(defn- as-app-options [args]
  (cli/cli args
           ["-m" "--module" "Input vcs module type: supports svn or git"]
           ["-o" "--output" "Output format: supports csv or graphs" :default "csv"]
           ["-a" "--analysis" "The analysis to run (authors, revisions, coupling, :all)"
            :default "authors"]
           ["-r" "--rows"   "Max rows in output" :default 10 :parse-fn #(Integer. %)]
           ["-e" "--max-entries" "Max entries to parse in the input log file"
            :parse-fn #(Integer. %)]
           ["-d" "--date" "The start date to consider in the logs, given as yyyyMMdd"
            :parse-fn #(time-format/parse (time-format/formatter "yyyyMMdd") %)]
           ["--min-revs" "Minimum number of revisions to include an entity in the analysis"
            :default 5 :parse-fn #(Integer. %)]
           ["--min-shared-revs" "Minimum number of shared revisions to include an entity in the analysis"
            :default 5 :parse-fn #(Integer. %)]
           ["--min-coupling" "Minimum degree of coupling (in percentage) to consider"
            :default 50 :parse-fn #(Integer. %)]))

(defn- print-banner []
  (let [[options args banner] (as-app-options [])]
    (println banner)))

;;;TODO: validate mandatory args, else throw!
(defn- input-file-from [args]
  (first args))

;;;TODO: introduce main catch block here - recovery point: print-banner
(defn -main
  ([]
     (print-banner))
  ([& args]
     (let [[options free-args banner]
           (as-app-options args)]
       (app/run (input-file-from free-args) options))))
