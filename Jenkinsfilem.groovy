#!groovy

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

@groovy.transform.Field
def status=[]

pipeline {
    options {
        buildDiscarder(logRotator(numToKeepStr: '2'))
        disableConcurrentBuilds()
        timeout(time: 180, unit: 'MINUTES')
    }
    agent { 
        node {
            
            label 'ubuntu' 
        }
    }
    stages {
        stage("Preparing Variable") {
            //agent { node { label 'ubuntu' } }
            steps {
                script {
                    // more or less  from puppet 
                    def versions = [
  "jdk_1.4_latest": "latest1.4",
  "jdk_1.5_latest": "latest1.5",
  "jdk_1.6_latest": "latest1.6",
  "jdk_1.7_latest": "latest1.7",
  "jdk_1.8_latest": "latest1.8",
  "jdk_1.9_latest": "latest1.9",
  "jdk_10_latest": "latest10",
  "jdk_11_latest": "latest11",
  "jdk_12_latest": "latest12",
  "jdk_13_latest": "latest13",
  "jdk_14_latest": "latest14",
  "jdk_15_latest": "latest15",
  "jdk_16_latest": "latest16",
  "jdk_17_latest": "latest17",
  "jdk_18_latest": "latest18",
  "jdk_19_latest": "latest19"]
                    def maven = [    'maven_2.2.1',
    'maven_3.0.5',
    'maven_3.1.1',
    'maven_3.2.5',
    'maven_3.3.9',
    'maven_3.5.4',
    'maven_3.6.3',
    'maven_3.8.1',
    'maven_3.8.3',
    'maven_3.8.4',
    'maven_3.8.5',
                    ]
                    def maven_latest = [    
'maven_latest',    
'maven_3_latest',   
'maven_2_latest',
                    ]

                    def ant = [
                       'ant_1.8.4'
                    ]
                    def ant_latest = [
                        'ant_latest'
                    ]
                    
                    def nodelist = nodesByLabel label:'ubuntu'
                    //debug def nodelist = ['ubuntu']
                    def testersunbuntu = [:]
                    for (anode in nodelist) {
                        def tnode = anode 
                        testersunbuntu[tnode] = {
                           
                            node (tnode) {   
                                // test each jdk
                                versions.each { ajdk,useless -> 
                                    stage("testing java "+ajdk){ 
                                        try {
                                            withEnv(["Path+JDK=${tool name: ajdk }/bin","JAVA_HOME=${tool name: ajdk }"]) {
                                                sh 'java -version'
                                                sh 'javac -version'
                                            } 
                                        } catch (e) {
                                            status.add("WIP JDK "+ajdk+ "error on node:"+tnode)
                                        }
                                    }
                                    
                                }
                                // test each maven on jdk 8
                                for (amaven in maven) {
                                    stage("testing maven "+ amaven){ 
                                        try {
                                            withMaven(jdk:'jdk_1.8_latest',maven: amaven) {
                                                sh 'mvn -version'
                                            
                                            } 
                                        } catch (e) {
                                            status.add("WIP Maven "+amaven+ "error on node:"+tnode)
                                        }
                                    }
                                }
                               
                                // test each maven latest on jdk 8
                                for (amaven in maven_latest) {
                                    stage("testing maven "+ amaven){ 
                                        try {
                                            withMaven(jdk:'jdk_1.8_latest',maven: amaven) {
                                                sh 'mvn -version'
                                            
                                            } 
                                        } catch (e) {
                                            status.add("WIP Maven "+amaven+ "error on node:"+tnode)
                                        }
                                    }
                                }
                                
                                // test each maven on jdk 8
                                for (aant in ant) {
                                    stage("testing ant "+ aant){ 
                                        try {
                                            withAnt(jdk:'jdk_1.8_latest',ant: aant) {
                                                sh 'ant -version'
                                            
                                            } 
                                        } catch (e) {
                                            status.add("WIP Ant "+aant+ "error on node:"+tnode)
                                        }
                                    }
                                }
                               
                                // test each maven latest on jdk 8
                                for (aant in ant_latest) {
                                    stage("testing ant "+ aant){ 
                                        try {
                                            withAnt(jdk:'jdk_1.8_latest',ant: aant) {
                                                sh 'ant -version'                                            
                                            } 
                                        } catch (e) {
                                            status.add("WIP Ant "+aant+ "error on node:"+tnode)
                                        }
                                    }
                                }
                                                               
                            }
                        }
                    }
                    testersunbuntu.failFast =  false
                    // parallel on each node 
                    parallel testersunbuntu
                    
                    /*
                    def nodelist = nodesByLabel label:'Windows'
                    def testerswindows = [:]
                    for (anode in nodelist) {
                    def tnode = anode 
                    testerswindows[tnode] = {
                    node(tnode) {
                    // build steps that should happen on all nodes go here
                    }
                    }
                    } 
                    parallel testerswindows*/
                    
                        
                    if (status.size() > 0 ) {
                        
                        error ("Issues detected\n"+status.join("\n")) 
                    }    
                        
                    
                    
                    
                    
                }
            }
        }
    }
    
}

