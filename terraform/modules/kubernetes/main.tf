locals {
  kubernetes_config_map = flatten([
    for idx in var.helm_service_template : [
      for key, value in idx.helm_chart_config_map : {
        key   = key,
        value = value
      }
    ]
  ])

}

resource "kubernetes_config_map" "configmap_service" {
  for_each = { for idx, service in var.helm_service_template : idx => service if service.is_there_config_map }
  metadata {
    name      = "cm-${each.value.name}"
    namespace = each.value.namespaces
  }

  data = { for idx in local.kubernetes_config_map : idx.key => idx.value }
}

resource "kubernetes_secret" "secret_services" {
  for_each = { for idx, service in var.helm_service_template : idx => service if service.is_there_secret }
  metadata {
    name      = "sc-${each.value.name}"
    namespace = each.value.namespaces
  }

  type = each.value.secret_type

  data = { for key, value in var.kubernetes_secrets_data : key => value }
}
