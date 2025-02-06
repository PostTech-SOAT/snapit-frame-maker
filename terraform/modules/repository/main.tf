resource "aws_ecr_repository" "hexburger_ecr" {
  name = var.application

  image_scanning_configuration {
    scan_on_push = true
  }

  image_tag_mutability = "IMMUTABLE"

  tags = {
    Name = "${var.application}-ecr"
  }
}
