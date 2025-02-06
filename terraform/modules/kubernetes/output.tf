output "config_map_name" {
  value = [for k in kubernetes_config_map.configmap_service : k.metadata.0.name]
}

output "secret_name" {
  value = [for k in kubernetes_secret.secret_services : k.metadata.0.name]
}

output "secret_data" {
  value = [for k in kubernetes_secret.secret_services : k.binary_data]

}
