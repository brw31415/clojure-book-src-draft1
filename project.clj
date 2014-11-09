(defproject hipstr "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [postgresql/postgresql "9.1-901-1.jdbc4"]
                 [lib-noir "0.8.6"]
                 [ring-server "0.3.1"]
                 [selmer "0.6.9"]
                 [com.taoensso/timbre "3.2.1"]
                 [com.taoensso/tower "2.0.2"]
                 [markdown-clj "0.9.47"]
                 [environ "0.5.0"]
                 [im.chit/cronj "1.0.1"]
                 [noir-exception "0.2.2"]
                 [korma "0.3.0"] ; a Clojre DSL for DB stuff.
                 [com.novemberain/validateur "2.3.1"]
                 [migratus "0.7.0"]                     ; used for db migrations
                 [yesql "0.5.0-beta3-SNAPSHOT"] ;[yesql "0.5.0-beta2"]                  ; a library for using SQL
                 [clj-time "0.8.0"]                     ; a library for sane date/time goodness
                 [crypto-password "0.1.3"]]             ; used for encrypting our passwords

  :repl-options {:init-ns hipstr.repl}
  :jvm-opts ["-server"]
  :plugins [[lein-ring "0.8.10"]
            [lein-environ "0.5.0"]
            [migratus-lein "0.1.0"]]
  :ring {:handler hipstr.handler/app
         :init    hipstr.handler/init
         :destroy hipstr.handler/destroy}
  :migratus {
             :store         :database
             :migration-dir "migrations"
             :migration-table-name "_migrations"
             :db            {:classname   "org.postgresql.Driver"
                             :subprotocol "postgresql"
                             :subname     "//localhost/postgres"
                             :user        "hipstr"
                             :password    "p455w0rd"}}
  :profiles
  {:uberjar {:aot :all}
   :production {:ring {:open-browser? false
                       :stacktraces?  false
                       :auto-reload?  false}}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.3.0"]
                        [pjstadig/humane-test-output "0.6.0"]]
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev? true
	             :db-classname "org.postgresql.Driver"
               :db-subprotocol "postgresql"
               :db-subname     "//localhost/postgres"
               :db-user        "hipstr"
               :db-password    "p455w0rd"}}}
  :min-lein-version "2.0.0")
