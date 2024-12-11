resource "aws_security_group" "futebol_sg" {
  name        = "futebol_sg"
  description = "futebol security group"
  vpc_id      = aws_vpc.futebol_vpc_1.id
}

resource "aws_security_group_rule" "futebol_sg_out_public" {
  from_port         = 0
  protocol          = "-1"
  security_group_id = aws_security_group.futebol_sg.id
  to_port           = 0
  type              = "egress"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_security_group_rule" "futebol_sg_in_ssh" {
  from_port         = 22
  protocol          = "tcp"
  security_group_id = aws_security_group.futebol_sg.id
  to_port           = 22
  type              = "ingress"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_security_group_rule" "futebol_sg_in_http" {
  from_port         = 80
  protocol          = "tcp"
  security_group_id = aws_security_group.futebol_sg.id
  to_port           = 80
  type              = "ingress"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_key_pair" "futebol_key" {
  key_name   = "futebol_key2"
  public_key = file("~/.ssh/futebol_key2.pub")
}