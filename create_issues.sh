#!/bin/bash

# Script: create_issues.sh
# Tự động tạo GitHub Issues từ TaskList.md sử dụng GitHub CLI (gh)
# Yêu cầu: đã cài đặt và đăng nhập gh, file TaskList.md đúng định dạng
# Create issues from TaskList.md using GitHub CLI by parsing the file
# Create with AI Prompt: Bạn có thể tạo các issues(Task List) được mô tả ở phần "Describe the solution you'd like", Mỗi Issues(Task) hãy mô tả chi tiêu từ Yêu cầu, và out-put cần thiết
# git add TaskList.md && git commit -m "docs: add detailed task list including Git workflow and repository management tasks"
# git push origin main
#Bạn có thể tự động chuyển các task trong TaskList.md thành GitHub Issues bằng cách sử dụng GitHub CLI (gh) và một script nhỏ. Dưới đây là hướng dẫn chi tiết cho môi trường Linux:
#1. Cài đặt GitHub CLI (nếu chưa có)
#sudo apt update
#sudo apt install gh -y
#2. Đăng nhập GitHub CLI
#gh auth login
#Chọn GitHub.com, chọn HTTPS, và làm theo hướng dẫn để đăng nhập.

#3. Tạo script tự động tạo Issues từ TaskList.md
#Tạo file script, ví dụ: create_issues.sh với nội dung sau:
#!/bin/bash

# Lấy từng task từ TaskList.md và tạo issue
#awk '/^## [0-9]+\./{if(title){print title"\n"body > "issue.tmp"; system("gh issue create --title \""title"\" --body-file issue.tmp"); body=""} title=substr($0,5); next} /^- \*\*Mô tả:/{body="**Mô tả:** "substr($0,11)"\n"} /^- \*\*Output:/{body=body"**Output:**\n"} /^  - /{body=body substr($0,5)"\n"} END{if(title){print title"\n"body > "issue.tmp"; system("gh issue create --title \""title"\" --body-file issue.tmp")}}' TaskList.md
#rm -f issue.tmp

#4. Cấp quyền thực thi và chạy script
#chmod +x create_issues.sh
#chmod +x create_issues.sh
#./create_issues.sh
#5. Kết quả
#Mỗi task trong TaskList.md sẽ được tạo thành một Issue trên repo GitHub của bạn.
#Bạn có thể kiểm tra tại: https://github.com/dng-ngothanhloi/isa701-csdl/issues
#Nếu bạn muốn mình tạo sẵn file script này trong workspace, hãy xác nhận để mình thực hiện!
#
REPO="dng-ngothanhloi/isa701-csdl"

awk '/^## [0-9]+\./{if(title){print title"\n"body > "issue.tmp"; system("gh issue create --repo '"REPO"' --title \""title"\" --body-file issue.tmp"); body=""} title=substr($0,5); next} /^- \*\*Mô tả:/{body="**Mô tả:** "substr($0,11)"\n"} /^- \*\*Output:/{body=body"**Output:**\n"} /^  - /{body=body substr($0,5)"\n"} END{if(title){print title"\n"body > "issue.tmp"; system("gh issue create --repo '"REPO"' --title \""title"\" --body-file issue.tmp")}}' TaskList.md

rm -f issue.tmp
