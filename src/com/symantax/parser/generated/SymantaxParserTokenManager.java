/* Generated By:JavaCC: Do not edit this line. SymantaxParserTokenManager.java */
package com.symantax.parser.generated;
import com.symantax.ast.factory.*;
import com.symantax.ast.node.*;
import com.symantax.ast.node.list.*;
import com.symantax.ast.node.literal.*;
import com.symantax.ast.node.literal.type.*;

/** Token Manager. */
public class SymantaxParserTokenManager implements SymantaxParserConstants
{

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x1000000000L) != 0L)
         {
            jjmatchedKind = 45;
            return 8;
         }
         if ((active0 & 0x10000000L) != 0L)
            return 26;
         if ((active0 & 0x64f00000000L) != 0L)
         {
            jjmatchedKind = 45;
            return 25;
         }
         if ((active0 & 0x20000000L) != 0L)
            return 27;
         return -1;
      case 1:
         if ((active0 & 0x65f00000000L) != 0L)
         {
            jjmatchedKind = 45;
            jjmatchedPos = 1;
            return 25;
         }
         return -1;
      case 2:
         if ((active0 & 0x65d00000000L) != 0L)
         {
            jjmatchedKind = 45;
            jjmatchedPos = 2;
            return 25;
         }
         if ((active0 & 0x200000000L) != 0L)
            return 25;
         return -1;
      case 3:
         if ((active0 & 0x41400000000L) != 0L)
         {
            jjmatchedKind = 45;
            jjmatchedPos = 3;
            return 25;
         }
         if ((active0 & 0x24900000000L) != 0L)
            return 25;
         return -1;
      case 4:
         if ((active0 & 0x40000000000L) != 0L)
            return 25;
         if ((active0 & 0x1400000000L) != 0L)
         {
            jjmatchedKind = 45;
            jjmatchedPos = 4;
            return 25;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 9:
         return jjMoveStringLiteralDfa1_0(0x4L);
      case 33:
         return jjStopAtPos(0, 7);
      case 34:
         return jjStartNfaWithStates_0(0, 28, 26);
      case 35:
         return jjStopAtPos(0, 9);
      case 36:
         return jjStopAtPos(0, 10);
      case 37:
         return jjStopAtPos(0, 11);
      case 38:
         return jjStopAtPos(0, 13);
      case 39:
         return jjStartNfaWithStates_0(0, 29, 27);
      case 40:
         return jjStopAtPos(0, 18);
      case 41:
         return jjStopAtPos(0, 19);
      case 42:
         return jjStopAtPos(0, 14);
      case 43:
         return jjStopAtPos(0, 16);
      case 44:
         return jjStopAtPos(0, 30);
      case 45:
         return jjStopAtPos(0, 15);
      case 46:
         return jjStopAtPos(0, 31);
      case 58:
         return jjStopAtPos(0, 27);
      case 59:
         return jjStopAtPos(0, 26);
      case 60:
         return jjStopAtPos(0, 24);
      case 61:
         return jjStopAtPos(0, 17);
      case 62:
         return jjStopAtPos(0, 25);
      case 64:
         return jjStopAtPos(0, 8);
      case 91:
         return jjStopAtPos(0, 22);
      case 93:
         return jjStopAtPos(0, 23);
      case 94:
         return jjStopAtPos(0, 12);
      case 96:
         return jjStopAtPos(0, 5);
      case 98:
         return jjMoveStringLiteralDfa1_0(0x800000000L);
      case 102:
         return jjMoveStringLiteralDfa1_0(0x40000000000L);
      case 105:
         return jjMoveStringLiteralDfa1_0(0x200000000L);
      case 109:
         return jjMoveStringLiteralDfa1_0(0x1000000000L);
      case 115:
         return jjMoveStringLiteralDfa1_0(0x400000000L);
      case 116:
         return jjMoveStringLiteralDfa1_0(0x20100000000L);
      case 117:
         return jjMoveStringLiteralDfa1_0(0x4000000000L);
      case 123:
         return jjStopAtPos(0, 20);
      case 125:
         return jjStopAtPos(0, 21);
      case 126:
         return jjStopAtPos(0, 6);
      default :
         return jjMoveNfa_0(5, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 32:
         if ((active0 & 0x4L) != 0L)
            return jjStopAtPos(1, 2);
         break;
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x40000000000L);
      case 110:
         return jjMoveStringLiteralDfa2_0(active0, 0x200000000L);
      case 111:
         return jjMoveStringLiteralDfa2_0(active0, 0x1800000000L);
      case 114:
         return jjMoveStringLiteralDfa2_0(active0, 0x20000000000L);
      case 115:
         return jjMoveStringLiteralDfa2_0(active0, 0x4000000000L);
      case 116:
         return jjMoveStringLiteralDfa2_0(active0, 0x400000000L);
      case 121:
         return jjMoveStringLiteralDfa2_0(active0, 0x100000000L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 100:
         return jjMoveStringLiteralDfa3_0(active0, 0x1000000000L);
      case 101:
         return jjMoveStringLiteralDfa3_0(active0, 0x4000000000L);
      case 108:
         return jjMoveStringLiteralDfa3_0(active0, 0x40000000000L);
      case 111:
         return jjMoveStringLiteralDfa3_0(active0, 0x800000000L);
      case 112:
         return jjMoveStringLiteralDfa3_0(active0, 0x100000000L);
      case 114:
         return jjMoveStringLiteralDfa3_0(active0, 0x400000000L);
      case 116:
         if ((active0 & 0x200000000L) != 0L)
            return jjStartNfaWithStates_0(2, 33, 25);
         break;
      case 117:
         return jjMoveStringLiteralDfa3_0(active0, 0x20000000000L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 101:
         if ((active0 & 0x100000000L) != 0L)
            return jjStartNfaWithStates_0(3, 32, 25);
         else if ((active0 & 0x20000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 41, 25);
         break;
      case 105:
         return jjMoveStringLiteralDfa4_0(active0, 0x400000000L);
      case 108:
         if ((active0 & 0x800000000L) != 0L)
            return jjStartNfaWithStates_0(3, 35, 25);
         break;
      case 115:
         if ((active0 & 0x4000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 38, 25);
         return jjMoveStringLiteralDfa4_0(active0, 0x40000000000L);
      case 117:
         return jjMoveStringLiteralDfa4_0(active0, 0x1000000000L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 101:
         if ((active0 & 0x40000000000L) != 0L)
            return jjStartNfaWithStates_0(4, 42, 25);
         break;
      case 108:
         return jjMoveStringLiteralDfa5_0(active0, 0x1000000000L);
      case 110:
         return jjMoveStringLiteralDfa5_0(active0, 0x400000000L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private int jjMoveStringLiteralDfa5_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 101:
         if ((active0 & 0x1000000000L) != 0L)
            return jjStartNfaWithStates_0(5, 36, 25);
         break;
      case 103:
         if ((active0 & 0x400000000L) != 0L)
            return jjStartNfaWithStates_0(5, 34, 25);
         break;
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_0(int startState, int curPos)
{
   //int[] nextStates; // not used
   int startsAt = 0;
   jjnewStateCnt = 26;
   int i = 1;
   jjstateSet[0] = startState;
   //int j; // not used
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 26:
                  if ((0xfffffffbffffffffL & l) != 0L)
                     jjCheckNAddStates(0, 2);
                  else if (curChar == 34)
                  {
                     if (kind > 43)
                        kind = 43;
                  }
                  break;
               case 5:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 39)
                        kind = 39;
                     jjCheckNAdd(10);
                  }
                  else if (curChar == 39)
                     jjCheckNAddStates(3, 5);
                  else if (curChar == 34)
                     jjCheckNAddStates(0, 2);
                  if (curChar == 48)
                     jjstateSet[jjnewStateCnt++] = 11;
                  break;
               case 27:
                  if ((0xffffff7fffffffffL & l) != 0L)
                     jjCheckNAddStates(3, 5);
                  else if (curChar == 39)
                  {
                     if (kind > 44)
                        kind = 44;
                  }
                  break;
               case 8:
               case 25:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 45)
                     kind = 45;
                  jjCheckNAdd(25);
                  break;
               case 10:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 39)
                     kind = 39;
                  jjCheckNAdd(10);
                  break;
               case 12:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 40)
                     kind = 40;
                  jjstateSet[jjnewStateCnt++] = 12;
                  break;
               case 13:
                  if (curChar == 48)
                     jjstateSet[jjnewStateCnt++] = 11;
                  break;
               case 14:
                  if (curChar == 34)
                     jjCheckNAddStates(0, 2);
                  break;
               case 16:
                  jjCheckNAddStates(0, 2);
                  break;
               case 17:
                  if ((0xfffffffbffffffffL & l) != 0L)
                     jjCheckNAddStates(0, 2);
                  break;
               case 18:
                  if (curChar == 34 && kind > 43)
                     kind = 43;
                  break;
               case 19:
                  if (curChar == 39)
                     jjCheckNAddStates(3, 5);
                  break;
               case 21:
                  jjCheckNAddStates(3, 5);
                  break;
               case 22:
                  if ((0xffffff7fffffffffL & l) != 0L)
                     jjCheckNAddStates(3, 5);
                  break;
               case 23:
                  if (curChar == 39 && kind > 44)
                     kind = 44;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 26:
                  if ((0xffffffffefffffffL & l) != 0L)
                     jjCheckNAddStates(0, 2);
                  else if (curChar == 92)
                     jjstateSet[jjnewStateCnt++] = 16;
                  break;
               case 5:
                  if ((0x7fffffe07fffffeL & l) != 0L)
                  {
                     if (kind > 45)
                        kind = 45;
                     jjCheckNAdd(25);
                  }
                  if (curChar == 109)
                     jjstateSet[jjnewStateCnt++] = 8;
                  else if (curChar == 112)
                     jjstateSet[jjnewStateCnt++] = 4;
                  break;
               case 27:
                  if ((0xffffffffefffffffL & l) != 0L)
                     jjCheckNAddStates(3, 5);
                  else if (curChar == 92)
                     jjstateSet[jjnewStateCnt++] = 21;
                  break;
               case 8:
                  if ((0x7fffffe07fffffeL & l) != 0L)
                  {
                     if (kind > 45)
                        kind = 45;
                     jjCheckNAdd(25);
                  }
                  if (curChar == 97)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               case 0:
                  if (curChar == 99 && kind > 37)
                     kind = 37;
                  break;
               case 1:
                  if (curChar == 105)
                     jjstateSet[jjnewStateCnt++] = 0;
                  break;
               case 2:
                  if (curChar == 108)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 3:
                  if (curChar == 98)
                     jjstateSet[jjnewStateCnt++] = 2;
                  break;
               case 4:
                  if (curChar == 117)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 6:
                  if (curChar == 110 && kind > 37)
                     kind = 37;
                  break;
               case 7:
                  if (curChar == 105)
                     jjstateSet[jjnewStateCnt++] = 6;
                  break;
               case 9:
                  if (curChar == 109)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               case 11:
                  if (curChar == 100)
                     jjCheckNAdd(12);
                  break;
               case 12:
                  if ((0x100002001000020L & l) == 0L)
                     break;
                  if (kind > 40)
                     kind = 40;
                  jjCheckNAdd(12);
                  break;
               case 15:
                  if (curChar == 92)
                     jjstateSet[jjnewStateCnt++] = 16;
                  break;
               case 16:
                  jjCheckNAddStates(0, 2);
                  break;
               case 17:
                  if ((0xffffffffefffffffL & l) != 0L)
                     jjCheckNAddStates(0, 2);
                  break;
               case 20:
                  if (curChar == 92)
                     jjstateSet[jjnewStateCnt++] = 21;
                  break;
               case 21:
                  jjCheckNAddStates(3, 5);
                  break;
               case 22:
                  if ((0xffffffffefffffffL & l) != 0L)
                     jjCheckNAddStates(3, 5);
                  break;
               case 24:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 45)
                     kind = 45;
                  jjCheckNAdd(25);
                  break;
               case 25:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 45)
                     kind = 45;
                  jjCheckNAdd(25);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 26:
               case 17:
               case 16:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjCheckNAddStates(0, 2);
                  break;
               case 27:
               case 22:
               case 21:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjCheckNAddStates(3, 5);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 26 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   15, 17, 18, 20, 22, 23, 
};

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, "\140", "\176", "\41", "\100", "\43", "\44", 
"\45", "\136", "\46", "\52", "\55", "\53", "\75", "\50", "\51", "\173", "\175", 
"\133", "\135", "\74", "\76", "\73", "\72", "\42", "\47", "\54", "\56", 
"\164\171\160\145", "\151\156\164", "\163\164\162\151\156\147", "\142\157\157\154", 
"\155\157\144\165\154\145", null, "\165\163\145\163", null, null, "\164\162\165\145", 
"\146\141\154\163\145", null, null, null, null, null, };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT", 
};
static final long[] jjtoToken = {
   0x3fffffffffe1L, 
};
static final long[] jjtoSkip = {
   0x1eL, 
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[26];
private final int[] jjstateSet = new int[52];
protected char curChar;
/** Constructor. */
public SymantaxParserTokenManager(SimpleCharStream stream){
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}

/** Constructor. */
public SymantaxParserTokenManager(SimpleCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 26; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/** Switch to specified lex state. */
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   final Token t;
   final String tokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   tokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, tokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  //int kind;
  Token specialToken = null;
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {   
   try   
   {     
      curChar = input_stream.BeginToken();
   }     
   catch(java.io.IOException e)
   {        
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 32 && (0x100002400L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

}
