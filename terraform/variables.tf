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

variable "AWS_ACCESS_KEY_ID" {
  type = string
}

variable "AWS_SECRET_ACCESS_KEY" {
  type = string
}

variable "AWS_REGION" {
  type = string
}

variable "AWS_SESSION_TOKEN" {
  type = string
}
