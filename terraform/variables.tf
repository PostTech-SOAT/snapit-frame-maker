variable "helm_service_template" {
  type = list(object({
    name                  = string
    namespaces            = string
    helm_chart_key_value  = map(string)
    helm_chart_config_map = map(string)
    is_there_config_map   = bool
    is_there_secret       = bool
    secret_type           = string
  }))
}
variable "application" {
  type = string
}
variable "aws_region" {
  type = string
}
variable "ingress_nginx_name" {
  type = string
}

