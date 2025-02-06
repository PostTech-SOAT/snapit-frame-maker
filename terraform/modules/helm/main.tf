locals {
  helm_chart_values_set = flatten([
    for idx in var.helm_service_template : [
      for key, value in idx.helm_chart_key_value : {
        key   = key
        value = tostring(value)
      }
    ]
  ])
}

resource "helm_release" "deployments" {
  for_each  = { for idx, value in var.helm_service_template : idx => value }
  name      = each.value.name
  namespace = each.value.namespaces
  chart     = "${path.module}/charts/snapit-media-chart-0.1.0.tgz"
  timeout   = 600
  values = [
    file("${path.module}/charts/values.yaml")
  ]

  dynamic "set" {
    for_each = { for idx in local.helm_chart_values_set : idx.key => idx.value }

    content {
      name  = set.key
      value = set.value
    }
  }

  set {
    name  = "image.repository"
    value = var.container_image_url
  }

  set {
    name  = "image.tag"
    value = var.container_image_tag
  }

  set {
    name  = "ingress.hosts[0].host"
    value = var.ingress_config_host
  }

  set {
    name  = "configMapName"
    value = tostring(var.config_map_name)
  }

  set {
    name  = "secretName"
    value = tostring(var.secret_name)
  }

}
