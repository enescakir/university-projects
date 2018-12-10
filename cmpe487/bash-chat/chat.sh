#!/usr/bin/env bash

## P2P Bash Chat App
## Author: Mustafa Enes Cakir (enes@cakir.web.tr)

## CONFIGS ##
DISCOVERY_PORT=5000
MESSAGE_PORT=5001
ADDRESS_BOOK=address_book.txt
CIPHERS=ciphers.txt
DISCOVERY_LOG=discovery_log.txt
MESSAGE_LOG=message_log.txt
CHAT_PREFIX=chat

## GLOBAL VARIABLES ##
USERNAME=""
LOCAL_IP=""
LOCAL_NET=""

## METHODS ##
# Remove old files and kill old chat clients
clearHistory() {
    pkill -f "nc -lk"
    rm -f ${ADDRESS_BOOK}
    rm -f ${CIPHERS}
    rm -f ${DISCOVERY_LOG}
    rm -f ${MESSAGE_LOG}
    rm -f "$CHAT_PREFIX"*.txt
}

# Create current profile
createProfile() {
    printf "\nEnter your username: "
    printf "$(tput bold)"
    read USERNAME
    printf "$(tput sgr0)"

    LOCAL_IP=$(ifconfig | sed -En 's/127.0.0.1//;s/.*inet (addr:)?(([0-9]*\.){3}[0-9]*).*/\2/p')
    LOCAL_NET=$(echo "$LOCAL_IP" | cut -d\. -f1-3)

    if [ -z "$LOCAL_IP" ]; then
        clear
        printError "You have to connect a network for using this chat client"
        sleep 3
        exit
    fi

    clear
    printProfile
}

# Print current profile to console
printProfile() {
    printHeader "YOUR PROFILE"
    printf "%045s\n" "" | sed -e "s/0/-/g"
    printf "| $(changeStyle bold "%-20s")| %-20s|\n" "Username" "$USERNAME"
    printf "%045s\n" "" | sed -e "s/0/-/g"
    printf "| $(changeStyle bold "%-20s")| %-20s|\n" "Local IP" "$LOCAL_IP"
    printf "%045s\n" "" | sed -e "s/0/-/g"
    printf "| $(changeStyle bold "%-20s")| %-20s|\n" "Local Network" "$LOCAL_NET"
    printf "%045s\n" "" | sed -e "s/0/-/g"
}

# Print text to console as header
printHeader() {
    printf "$(changeStyle header "\n\n=== $1 ===\n\n")"
}

# Ask user to enter for continue
enterContinue() {
    printf "$(changeStyle inverse "\n\n\nEnter to continue...")"
    read -r tmp
    clear
}

# Creates empty address book and cipher files
createFiles() {
    touch ${ADDRESS_BOOK}
    touch ${CIPHERS}
}

# Say hello to all user on network, brute force
informNetwork() {
    printf "$(changeStyle green "Informing all device at same network...\n")"
    
    for i in {1..254}
    do
        local target_ip="$LOCAL_NET.$i"

        if [ "$target_ip" != "$LOCAL_IP" ]
        then
            echo "0;$LOCAL_IP;$USERNAME;$target_ip;;" | nc -G 1 ${target_ip} ${DISCOVERY_PORT} &
            echo "0;$LOCAL_IP;$USERNAME;$target_ip;;" >> ${DISCOVERY_LOG}
        fi
    done
}

# Listening discovery port new packets
listenDiscovery() {
    printf "$(changeStyle green "Discovering on port $(changeStyle bold $DISCOVERY_PORT)\n")"

    nc -lk ${DISCOVERY_PORT} | receiveDiscovery
}

# Save new discovered user to address book
receiveDiscovery() {
    while read discovery_packet
    do
        local type=$(echo ${discovery_packet} | cut -d\; -f1)
        local source_ip=$(echo ${discovery_packet} | cut -d\; -f2)
        local source_name=$(echo ${discovery_packet} | cut -d\; -f3)
        local target_ip=$(echo ${discovery_packet} | cut -d\; -f4)
        local target_name=$(echo ${discovery_packet} | cut -d\; -f5)
        
        if [ "$type" == "1" ]
        then
            printNotification "$source_name is online"
            setName "$source_ip" "$source_name"
        elif [ "$type" == "0" ]
        then
            printNotification "$source_name open chat client"
            setName "$source_ip" "$source_name"
            echo "1;$LOCAL_IP;$USERNAME;$source_ip;$source_name;" | nc -G 1 ${source_ip} ${DISCOVERY_PORT} &
            echo "1;$LOCAL_IP;$USERNAME;$source_ip;$source_name;" >> ${DISCOVERY_LOG}
        else
            printError "Unknown Discovery Packet"
        fi
        
        echo "$discovery_packet" >> ${DISCOVERY_LOG}
    done
}

# Listening message port new packets
listenMessage() {
    printf "$(changeStyle green "Listening messages on port $(changeStyle bold $MESSAGE_PORT)\n")"
    nc -lk ${MESSAGE_PORT} | receiveMessage
}

# Save new sent message to chat history
receiveMessage() {
    while read message_packet
    do
        local source_ip=$(echo "$message_packet" | cut -d\; -f1)
        local source_cipher=$(echo "$message_packet" | cut -d\; -f2)
        local body=$(echo "$message_packet" | cut -d\; -f3)

        local target_cipher=$(getCipher ${source_ip})
        
        if [  -z "$target_cipher" ] || [ "$(getMD5 "$target_cipher")" == "$source_cipher" ]
        then
            setCipher "$source_ip" "$source_cipher"
            echo "0;$body" >> "$(getChat ${source_ip})"
            printNotification "New message from $(getName ${source_ip})"
            body="VALID;$message_packet"
        else
            body="NOTVALID;$message_packet"
            printError "Not valid message from ${source_ip}"
        fi
        
        echo "$body" >> ${MESSAGE_LOG}
    done
}

# Main command menu of the application
enterCommand() {
    printHeader "AVAILABLE COMMANDS"
    
    commands=("List online users" "Send message" "Show my profile" "Discover user" "Quit")
    
    for i in "${!commands[@]}"; do
        printf "\t$(changeStyle bold "$(($i+1)))") ${commands[$i]} \n"
    done
    
    printf "\nPlease enter your command: "
    printf "$(tput bold)"
    read -r option
    printf "$(tput sgr0)"
    
    case ${option} in
        1) clear; printUsers; enterContinue;;
        2) clear; selectChat;;
        3) clear; printProfile; enterContinue;;
        4) clear; discoverUser;;
        5) clear; clearHistory; printNotification "Good bye $USERNAME \n\n"; break;;
        *) clear; printError "Invalid option";;
    esac
}

# Print online users to console
printUsers() {
    printHeader "ONLINE USERS"
    printf "%045s\n" "" | sed -e "s/0/-/g"
    printf "| %-20s| %-20s|\n" "IP" "NAME"
    printf "|%021s|%021s|\n" "" "" | sed -e "s/0/-/g"
    cat ${ADDRESS_BOOK} |  sort -t. -k 4n | while read -r user ; do
        printf "| $(changeStyle bold "%-20s")| $(changeStyle green "%-20s")|\n" "$(echo "$user" | cut -d" " -f1)" "$(echo "$user" | cut -d" " -f2)"
    done
    printf "%045s\n" "" | sed -e "s/0/-/g"
}

# Print online users to console and ask to select for entering chat
selectChat() {
    printHeader "SELECT ONLINE USER"
    
    printf "\t$(changeStyle bold "%-5s") Go back\n" "0)"
    cat ${ADDRESS_BOOK} |  sort -t. -k 4n | while read -r user ; do
        printf "\t$(changeStyle bold "%-5s") $(echo "$user" | cut -d" " -f2) \n" "$(echo "$user" | cut -d" " -f1 | cut -d"." -f4))"
    done
    
    printf "\nEnter user ID: "
    printf "$(tput bold)"
    read -r id
    printf "$(tput sgr0)"
    
    if [ "$id" == "0" ]
    then
        clear
    else
        showChat ${id}
    fi
}

# Show messages of given chat and ask for new message
showChat() {
    clear
    local id=$1
    local target_ip="$LOCAL_NET.$id"
    local chat="$(getChat "$target_ip")"
    local target_name="$(getName "$target_ip")"
    
    if [ -z "$target_name" ]
    then
        printError "Please enter valid user ID"
        selectChat
    else
        printHeader "CHAT WITH $target_name"

        if [ ! -f "$chat" ]; then
            printError "You don't have any messages history with $target_name"
        else
            printf "\n"
            cat ${chat} | sed -e "s/1;/  $(changeStyle receiver "$USERNAME:")  /g" | sed -e "s/0;/  $(changeStyle sender "$target_name:") /g"
        fi
        
        printf "\n\nEnter your message [write 'exit' for go back]: \n> "
        printf "$(tput bold)"
        read -r body
        printf "$(tput sgr0)"

        if [ "$body" != "exit" ]
        then
            if [ ! -z "$body" ]
            then
                # SEND MESSAGE
                echo "1;$body" >> "$chat"
                
                local target_cipher=$(getCipher ${target_ip})
                local new_cipher=$(getMD5 ${target_cipher})
                
                if [ -z "$target_cipher" ]
                then
                    new_cipher="$(getMD5 "test")"
                fi
                
                setCipher ${target_ip} "$new_cipher"
                
                echo "$LOCAL_IP;$new_cipher;$body" | nc -G 2 "$target_ip" ${MESSAGE_PORT} &
                echo "$LOCAL_IP;$new_cipher;$body" >> ${MESSAGE_LOG}

            fi
            showChat ${id}
        fi
        clear
    fi
}

# Discover user manually
discoverUser() {
    printHeader "DISCOVER USER"
        
    printf "\nEnter user IP for discovery: "
    printf "$(tput bold)"
    read -r ip
    printf "$(tput sgr0)"
    
    echo "0;$LOCAL_IP;$USERNAME;$ip;;" | nc -G 1 ${ip} ${DISCOVERY_PORT}
    echo "0;$LOCAL_IP;$USERNAME;$ip;;" >> ${DISCOVERY_LOG}

    printf "Discovering $ip..."

    enterContinue
}

# Prints notification to the top of console
# $1 => Message to print
printNotification() {
    printf "\e[s \e[100F \e[2K\r $(changeStyle bold "[!]") $(changeStyle success " $1 ") \e[u";
}

# Prints error to the top of console
# $1 => Error to print
printError() {
    printf "\e[s \e[100F \e[2K\r $(changeStyle bold "[x]") $(changeStyle error " $1 ") \e[u";
}

# Returns MD5 hashed version of given string
# $1 => String to hashed
getMD5() {
    echo "$(md5 -s "$1" | cut -d"=" -f2 | sed -e 's/ //')"
}

# Returns chat file name of given ip
# $1 => IP address of user
getChat() {
    echo "$CHAT_PREFIX$(echo $1 | cut -d\. -f3-4 | sed -e 's/\./_/').txt"
}

# Returns user name of given ip
# $1 => IP address of user
getName() {
    echo "$(cat ${ADDRESS_BOOK} | grep -w "$1" | cut -d " " -f2)"
}

# Changes name of the given IP
# $1 => IP address of user
# $2 => name of user
setName() {
    local name=$(getName $1)
    if [ -z "$name" ]
    then
        echo "$1 $2" >> ${ADDRESS_BOOK}
    else
        sed -i '' -e 's/^'$1' .*$/'$1' '$2'/' ${ADDRESS_BOOK}
    fi
}

# Returns cipher of given ip
# $1 => IP address of user
getCipher() {
    echo "$(cat ${CIPHERS} | grep -w "$1" | cut -d " " -f2)"
}

# Changes cipher of the given IP
# $1 => IP address of user
# $2 => cipher of user
setCipher() {
    local cipher=$(getCipher $1)
    if [ -z "$cipher" ]
    then
        echo "$1 $2" >> ${CIPHERS}
    else
        sed -i '' -e 's/^'$1' .*$/'$1' '$2'/' ${CIPHERS}
    fi
}

# Change style of text
# $1 => style: red, green, bold
# $2 => text to change style
changeStyle() {
    case $1 in
        "bold") echo "$(tput bold)$2$(tput sgr0)";;
        "underline") echo "$(tput smul)$2$(tput sgr0)";;
        "inverse") echo "$(tput rev)$2$(tput sgr0)";;
        "red") echo "$(tput setaf 1)$2$(tput setaf 9)$(tput sgr0)";;
        "green") echo "$(tput setaf 2)$2$(tput setaf 9)$(tput sgr0)";;
        "error") echo "$(tput rev)$(tput setaf 1)$2$(tput setaf 9)$(tput sgr0)";;
        "success") echo "$(tput rev)$(tput setaf 2)$2$(tput setaf 9)$(tput sgr0)";;
        "header") echo "$(tput bold)$(tput setaf 4)$2$(tput setaf 9)$(tput sgr0)";;
        "sender") echo "$(tput setaf 2)$(tput bold)$2$(tput setaf 9)$(tput sgr0)";;
        "receiver") echo "$(tput setaf 6)$(tput bold)$2$(tput setaf 9)$(tput sgr0)";;
    esac
}


## BEGINNING OF APPLICATION ##
printHeader "WELCOME TO P2P BASH CHAT APP"

clearHistory
createFiles
createProfile
enterContinue
listenDiscovery &
informNetwork &
sleep 1
listenMessage &
sleep 1
clear

while [ true ]
do
    enterCommand
done

