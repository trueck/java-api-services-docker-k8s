To build and run and test the services with docker compose

1. Start Docker Desktop

2. Build services:

```./build.sh```

3. Build docker images:

```./dockerBuild.sh```

4. Test services using docker-compose

```./test-em-all-docker.sh start stop```

5. other commands for docker compose

```
./dockerStart.sh
./dockerStop.sh
./whatsRunningDocker.sh
```


To run and test the services with k8s (minikube):

1. start minikube if it is not up:

```minikube start```

2. Create a name space:

```kubectl create namespace hands-on```

3. Activate ns

```kubectl config set-context $(kubectl config current-context) --namespace=hands-on```

4. Link docker with Minikube

```eval $(minikube -p minikube docker-env)```

5. build the images again so that minikube can see those images

```./dockerBuild.sh```

6. Deploy k8s using customize

```kubectl apply -k kubernetes/services/overlays/dev```

7. Check if all pod are ready

```kubectl wait --timeout=600s --for=condition=ready pod --all```

8. From localhost to access k8s service by getting url:

```minikube service product-composite --url -n hands-on```

9. Test services in k8s

```HOST=127.0.0.1 PORT=56906  ./test-em-all-docker.sh```

10. Remove services

```kubectl delete -k kubernetes/services/overlays/dev```


