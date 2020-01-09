#include <stdio.h>
#include <ctype.h>
#include "nodes.h"
#include "C.tab.h"
#include <string.h>
#include <stdlib.h>
#include "Parser.h"

extern int yydebug;

extern NODE *yyparse(void);

extern NODE *ans;

extern void init_symbtable(void);

int initialised = 0;

jobject convertToken(JNIEnv *env, TOKEN* token);
jobject convertNode(JNIEnv *env, NODE* node);

jobject convertToken(JNIEnv *env, TOKEN* token) {
    jclass resultClass = (*env)->FindClass(env, "com/tobi/mc/parser/Node");
    jobject newObj = (*env)->AllocObject(env, resultClass);

    jclass nodeTypeClass = (*env)->FindClass(env, "com/tobi/mc/parser/NodeType");
    jmethodID findByIdMethod = (*env)->GetStaticMethodID(env, nodeTypeClass, "getById", "(I)Lcom/tobi/mc/parser/NodeType;");
    jobject nodeType = (*env)->CallStaticObjectMethod(env, nodeTypeClass, findByIdMethod, token->type);

    jfieldID typeField = (*env)->GetFieldID(env, resultClass, "type", "Lcom/tobi/mc/parser/NodeType;");
    (*env)->SetObjectField(env, newObj, typeField, nodeType);

    jfieldID typeIdField = (*env)->GetFieldID(env, resultClass, "typeId", "I");
    (*env)->SetIntField(env, newObj, typeIdField, token->type);

    jfieldID lexemeField = (*env)->GetFieldID(env, resultClass, "lexeme", "Ljava/lang/String;");
    jfieldID valueField = (*env)->GetFieldID(env, resultClass, "value", "I");
    if(token->lexeme != NULL) {
        jstring lexemeString = (*env)->NewStringUTF(env, token->lexeme);
        (*env)->SetObjectField(env, newObj, lexemeField, lexemeString);
    }
    (*env)->SetIntField(env, newObj, valueField, token->value);

//    if(token->next != NULL) {
//        jfieldID nextField = (*env)->GetFieldID(env, resultClass, "left", "Lcom/tobi/mc/parser/Node;");
//        jobject nextNode = convertToken(env, token->next);
//        (*env)->SetObjectField(env, newObj, nextField, nextNode);
//    }

    return newObj;
}

jobject convertNode(JNIEnv *env, NODE* node) {
    if(node == NULL) return NULL;
    jclass resultClass = (*env)->FindClass(env, "com/tobi/mc/parser/Node");
    jobject newObj = (*env)->AllocObject(env, resultClass);

    jclass nodeTypeClass = (*env)->FindClass(env, "com/tobi/mc/parser/NodeType");
    jmethodID findByIdMethod = (*env)->GetStaticMethodID(env, nodeTypeClass, "getById", "(I)Lcom/tobi/mc/parser/NodeType;");
    jobject nodeType = (*env)->CallStaticObjectMethod(env, nodeTypeClass, findByIdMethod, node->type);

    jfieldID typeField = (*env)->GetFieldID(env, resultClass, "type", "Lcom/tobi/mc/parser/NodeType;");
    (*env)->SetObjectField(env, newObj, typeField, nodeType);

    jfieldID typeIdField = (*env)->GetFieldID(env, resultClass, "typeId", "I");
    (*env)->SetIntField(env, newObj, typeIdField, node->type);

    jfieldID leftField = (*env)->GetFieldID(env, resultClass, "left", "Lcom/tobi/mc/parser/Node;");

    if(node->type == LEAF) {
        TOKEN* token = (TOKEN*) node->left;
        jobject tokenObj = convertToken(env, token);
        (*env)->SetObjectField(env, newObj, leftField, tokenObj);
    } else {
        jfieldID rightField = (*env)->GetFieldID(env, resultClass, "right", "Lcom/tobi/mc/parser/Node;");

        jobject left = convertNode(env, node->left);
        jobject right = convertNode(env, node->right);

        if(left != NULL) {
            (*env)->SetObjectField(env, newObj, leftField, left);
        }
        if(right != NULL) {
            (*env)->SetObjectField(env, newObj, rightField, right);
        }
    }
    return newObj;
}

JNIEXPORT jobject JNICALL Java_com_tobi_mc_parser_Parser_parseProgram(JNIEnv *env, jobject thisObject) {
    jclass resultClass = (*env)->FindClass(env, "com/tobi/mc/parser/Node");

    if(!initialised) {
        init_symbtable();
        initialised = 1;
    }
    yyparse();
    return convertNode(env, ans);
}
