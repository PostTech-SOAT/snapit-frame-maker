##############################################################################
#                      GENERAL                                               #
##############################################################################

application = "snapit-frame-maker"
aws_region  = "us-east-1"

##############################################################################
#                      HELM                                                  #
##############################################################################

ingress_nginx_name = "ingress-nginx-controller"

helm_service_template = [{
  name                = "snapit-frame-maker"
  namespaces          = "snapit"
  is_there_config_map = true
  is_there_secret     = true
  secret_type         = "Opaque"

  helm_chart_key_value = {
    "chartName"                                     = "snapit-frame-maker"
    "serviceAccount.create"                         = "true"
    "serviceAccount.name"                           = "snapit-frame-maker-svc-acc"
    "service.type"                                  = "NodePort"
    "service.port"                                  = "28083"
    "service.targetPort"                            = "28083"
    "ingress.enabled"                               = "false"
    "image.pullPolicy"                              = "Always"
    "resources.requests.cpu"                        = "100m"
    "resources.requests.memory"                     = "1024Mi"
    "resources.limits.cpu"                          = "200m"
    "resources.limits.memory"                       = "1024Mi"
    "livenessProbe.initialDelaySeconds"             = "60"
    "livenessProbe.periodSeconds"                   = "60"
    "livenessProbe.timeoutSeconds"                  = "30"
    "readinessProbe.initialDelaySeconds"            = "40"
    "readinessProbe.periodSeconds"                  = "60"
    "readinessProbe.timeoutSeconds"                 = "30"
    "autoscaling.enabled"                           = "true"
    "autoscaling.minReplicas"                       = "2"
    "autoscaling.maxReplicas"                       = "2"
    "autoscaling.targetCPUUtilizationPercentage"    = "70"
    "autoscaling.targetMemoryUtilizationPercentage" = "70"
    "configMap.enabled"                             = "false"
    "nameOverride"                                  = "snapit-frame-maker"
    "fullnameOverride"                              = "snapit-frame-maker-api"
  }

  helm_chart_config_map = {
    "APPLICATION_NAME" = "snapit-frame-maker"
    "API_DOCS_PATH"    = "/api-docs"
    "API_PORT"         = "28083"
    "DATABASE_URL"     = "jdbc:postgresql://snapit-db.cq23vjwswp3a.us-east-1.rds.amazonaws.com:5432/postgres"
  }


}]
