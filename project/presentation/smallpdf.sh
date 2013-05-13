#!/bin/sh

latex $1
dvips -Ppdf -t landscape $1
dvipdf $1

